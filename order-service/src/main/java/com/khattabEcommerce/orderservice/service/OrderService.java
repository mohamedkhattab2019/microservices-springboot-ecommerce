package com.khattabEcommerce.orderservice.service;

import com.khattabEcommerce.orderservice.dto.InventoryResponse;
import com.khattabEcommerce.orderservice.dto.OrderLineItemsDto;
import com.khattabEcommerce.orderservice.dto.OrderRequest;
import com.khattabEcommerce.orderservice.model.Order;
import com.khattabEcommerce.orderservice.model.OrderLineItems;
import com.khattabEcommerce.orderservice.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public OrderService(ModelMapper modelMapper, OrderRepository orderRepository, WebClient.Builder webClientBuilder) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public String placeOrder(@RequestBody OrderRequest orderRequest){
        System.out.println(orderRequest);
        System.out.println(orderRequest.getOrderLineItemsDtoList());
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems =  orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto).collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).collect(Collectors.toList());
        // Call Inventory service, and place order if order is in stock
        InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();


        assert inventoryResponsesArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);
        if(allProductsInStock){
            orderRepository.save(order);
            return "order Placed successfully";
        }
        else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        return modelMapper.map(orderLineItemsDto,OrderLineItems.class);
    }
}
