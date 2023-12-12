package com.khattabEcommerce.product.service.repository;

import com.khattabEcommerce.product.service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String > {
}
