package com.worldfirst.fx.repository;


import com.worldfirst.fx.config.AsyncConfiguration;
import com.worldfirst.fx.enums.OrderStatus;
import com.worldfirst.fx.enums.OrderType;
import com.worldfirst.fx.model.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class OrderRepository {
    private ConcurrentHashMap<String, Order> orderMap = new ConcurrentHashMap<String, Order>();

    public CompletableFuture<Optional<Order>> create(Order order) {
        orderMap.putIfAbsent(order.getId(),order);
        Order r  = orderMap.get(order.getId());
        return CompletableFuture.completedFuture(Optional.ofNullable(orderMap.get(order.getId())));
    }

    public CompletableFuture<Optional<Order>> findById(String id) {
        Order order = orderMap.get(id);
        return CompletableFuture.completedFuture(Optional.ofNullable(order));
    }

    public CompletableFuture<Collection<Order>> findAll() {
        return CompletableFuture.completedFuture(orderMap.values());
    }

    public CompletableFuture<Optional<Order>> update(Order order) {
        Order updatedOrder = null;
        if(orderMap.containsKey(order.getId())){
            updatedOrder = orderMap.putIfAbsent(order.getId(),order);
            return CompletableFuture.completedFuture(Optional.ofNullable(updatedOrder));
        }
        updatedOrder = orderMap.put(order.getId(),order);
        return CompletableFuture.completedFuture(Optional.ofNullable(updatedOrder));
    }

    public synchronized CompletableFuture<Boolean> delete(Order order) {
            Order orderToDel = orderMap.remove(order);
        Optional<Order> deletedOrder = Optional.ofNullable(orderToDel);
        if(deletedOrder.isPresent()){
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        return CompletableFuture.completedFuture(Boolean.FALSE);
    }

    public List<Order> findByOrderTypeAndStatus(OrderType orderType, OrderStatus orderStatus) {
        return orderMap.values().stream().filter(order ->
                                        (order.getOrderType().equals(orderType) && order.getOrderStatus().equals(orderStatus))
                                            ).collect(Collectors.toList());
    }
}
