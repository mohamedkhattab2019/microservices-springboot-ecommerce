package com.khattabEcommerce.product.service;

import com.khattabEcommerce.product.service.dto.ProductRequest;
import com.khattabEcommerce.product.service.model.Product;
import com.khattabEcommerce.product.service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper; // pojo to json and json to pojo
    @Autowired
    private ProductRepository productRepository;

     /*
     * At start a time of starting the integration test.Firstly the test will start the mongodb container by downloading mongo:4.4.2 image
     * Then it will get the replica Set URL and add it to the spring data mongodb url prop dynamically at the time of creating the test
      */
    @Container // JUnit will understand that this is a mongodb container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
    static {
        mongoDBContainer.start();
    }


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.data.mongodb.url",mongoDBContainer::getReplicaSetUrl);

    }

    @Test
    void ShouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestString))
                .andExpect(status().isCreated());

//        Assertions.assertTrue(productRepository.findAll().size() == 1);
        Assertions.assertEquals(10,productRepository.findAll().size());
    }

    private ProductRequest getProductRequest() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Iphone 15");
        productRequest.setDescription("Iphone 15 Desc");
        productRequest.setPrice(BigDecimal.valueOf(1200));

        return productRequest;
    }

}
