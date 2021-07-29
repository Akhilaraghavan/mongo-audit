package com.aragh.audit.config;

import com.aragh.audit.model.Product;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;


@Configuration
public class RepositoryRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setBasePath("/data");
        config.setReturnBodyOnCreate(true);
        config.setReturnBodyOnUpdate(true);
        config.exposeIdsFor(Product.class);
    }
}
