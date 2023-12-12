package com.khattabEcommerce.product.service.service;

import com.khattabEcommerce.product.service.dto.ProductRequest;
import com.khattabEcommerce.product.service.dto.ProductResponse;
import com.khattabEcommerce.product.service.model.Product;
import com.khattabEcommerce.product.service.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private ModelMapper modelMapper;
    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public ProductResponse createProduct(ProductRequest productRequest){
        Product product = modelMapper.map(productRequest, Product.class);
        Product newProduct = productRepository.save(product);

        return modelMapper.map(newProduct, ProductResponse.class);
//        log.info("product {}  is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
       return products.stream().map(product -> mapToProductResponse(product)).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return modelMapper.map(product,ProductResponse.class);
    }
}
