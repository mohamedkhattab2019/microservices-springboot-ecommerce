package com.khattabEcommerce.inventoryservice.service;

import com.khattabEcommerce.inventoryservice.dto.InventoryResponse;
import com.khattabEcommerce.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
        InventoryResponse inventoryResponse = new InventoryResponse();
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> {
                    inventoryResponse.setSkuCode(inventory.getSkuCode());
                    inventoryResponse.setInStock(inventory.getQuantity() > 0);

                    return inventoryResponse;
                }).collect(Collectors.toList());
    }
}
