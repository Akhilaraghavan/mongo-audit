package com.aragh.audit.security;

import com.aragh.audit.model.User;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {

    public Authentication parseToken(String token)  {
        try {

            URL publicKey = Thread.currentThread().getContextClassLoader().getResource("public_key.pem");
            assert publicKey != null;

            KeyPair keyPair = getKeyPair(null, Paths.get(publicKey.toURI()), "RSA");

            // parse the token.
            Jwt jwt = Jwts.parser()
                    .setSigningKey(keyPair.getPublic())
                    .parse(token.split(" ")[1]);

            DefaultClaims defaultClaims = (DefaultClaims) jwt.getBody();
            User user = new User();
            user.setUsername(defaultClaims.getSubject());
            List<String> roles = (List<String>) defaultClaims.getOrDefault("roles", Arrays.asList("READ_PRIVILEDGE"));
            return new UsernamePasswordAuthenticationToken(user, token, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        } catch (NoSuchAlgorithmException | IOException | URISyntaxException | InvalidKeySpecException e) {
           log.error("Error parsing jwt token {}", e.getMessage(), e);
           throw new JwtTokenException("Error parsing jwt token " + e.getMessage(), e);
        }
    }

    public Authentication parseTokenSecret(String token)  {
            // parse the token.
            Jwt jwt = Jwts.parser()
                    .setSigningKey("secret")
                    .parse(token.split(" ")[1]);

            DefaultClaims defaultClaims = (DefaultClaims) jwt.getBody();
            User user = new User();
            user.setUsername(defaultClaims.getSubject());
            List<String> roles = (List<String>) defaultClaims.getOrDefault("roles", Arrays.asList("READ_PRIVILEDGE"));
            return new UsernamePasswordAuthenticationToken(user, token, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

    }

    private KeyPair getKeyPair(Path privateKeyPath, Path publicKeyPath, String algorithm) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PrivateKey privateKey = null;
        PublicKey publicKey = null;

        if (privateKeyPath != null) {
            byte[] encodedPrivateKey = IOUtils.toByteArray(privateKeyPath.toUri());
            String key = new String(encodedPrivateKey, 0, encodedPrivateKey.length).replaceAll("-----BEGIN (.*)-----", "")
                    .replaceAll("-----END (.*)----", "")
                    .replaceAll("\r\n", "")
                    .replaceAll("\n", "")
                    .trim();
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
            privateKey= keyFactory.generatePrivate(spec);
        }

        if (publicKeyPath != null) {
            byte[] encodedPublicKey = IOUtils.toByteArray(publicKeyPath.toUri());
            String key = new String(encodedPublicKey, 0, encodedPublicKey.length).replaceAll("-----BEGIN (.*)-----", "")
                    .replaceAll("-----END (.*)----", "")
                    .replaceAll("\r\n", "")
                    .replaceAll("\n", "")
                    .trim();
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
            publicKey = keyFactory.generatePublic(publicKeySpec);
        }

        return new KeyPair(publicKey, privateKey);
    }
}
