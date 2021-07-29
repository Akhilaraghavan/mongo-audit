package com.aragh.audit.repository;

import com.aragh.audit.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
@PreAuthorize("hasAnyAuthority('READ_PRIVILEDGE, WRITE_PRIVILEDGE')")
public interface ProductRepository extends MongoRepository<Product, String> {
}
