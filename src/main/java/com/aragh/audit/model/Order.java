package com.aragh.audit.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
@Data
public class Order {

    @Id
    private String id;

    private String productId;

    private int productCount;
}
