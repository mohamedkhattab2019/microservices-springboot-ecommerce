package com.khattabEcommerce.inventoryservice.service;

import com.khattabEcommerce.inventoryservice.dto.InventoryResponse;
import com.khattabEcommerce.inventoryservice.repository.InventoryRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    @Transactional(readOnly = true)
//    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode){
//        log.info( "Started");
//        Thread.sleep(10000);
//        log.info( "Ended");

        InventoryResponse inventoryResponse = new InventoryResponse();
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> {
                    inventoryResponse.setSkuCode(inventory.getSkuCode());
                    inventoryResponse.setInStock(inventory.getQuantity() > 0);

                    return inventoryResponse;
                }).collect(Collectors.toList());
    }
}
