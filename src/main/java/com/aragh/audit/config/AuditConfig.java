package com.aragh.audit.config;

import com.aragh.audit.security.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class AuditConfig {

    @Bean
    public SecurityAuditorAware auditorAware() {
        return new SecurityAuditorAware();
    }
}
