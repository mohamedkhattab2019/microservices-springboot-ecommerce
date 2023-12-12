package com.khattabEcommerce.orderservice.service;

import com.khattabEcommerce.orderservice.dto.OrderLineItemsDto;
import com.khattabEcommerce.orderservice.dto.OrderRequest;
import com.khattabEcommerce.orderservice.model.Order;
import com.khattabEcommerce.orderservice.model.OrderLineItems;
import com.khattabEcommerce.orderservice.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    public OrderService(ModelMapper modelMapper, OrderRepository orderRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
    }

    public void placeOrder(@RequestBody OrderRequest orderRequest){
        System.out.println(orderRequest);
        System.out.println(orderRequest.getOrderLineItemsDtoList());
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems =  orderRequest.getOrderLineItemsDtoList().stream().map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        // Call Inventory service, and place order if order is in stock

        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = modelMapper.map(orderLineItemsDto,OrderLineItems.class);

        return orderLineItems;
    }
}
