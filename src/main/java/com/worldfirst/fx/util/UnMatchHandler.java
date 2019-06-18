package com.worldfirst.fx.util;

import com.sun.tools.corba.se.idl.constExpr.Or;
import com.worldfirst.fx.enums.OrderStatus;
import com.worldfirst.fx.enums.OrderType;
import com.worldfirst.fx.model.Order;
import com.worldfirst.fx.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UnMatchHandler {

    @Autowired
    private OrderRepository orderRepository;

    @Value( "${fx.current.price:1.2100}")
    private Double currentPrice;

    public CompletableFuture<Optional<List<Order>>> getUnMatchedOrders() {
        List<Order> unmatchedOrders = null;
        List<Order> pendingAskOrders = orderRepository.findByOrderTypeAndStatus(OrderType.ASK,
                OrderStatus.PENDING);
        unmatchedOrders = pendingAskOrders.stream()
                .filter(order -> order.getPrice() > currentPrice
                ).collect(Collectors.toList());
        return CompletableFuture.completedFuture(Optional.ofNullable(unmatchedOrders));
    }
}
