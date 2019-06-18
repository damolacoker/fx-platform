package com.worldfirst.fx.service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.worldfirst.fx.enums.OrderStatus;
import com.worldfirst.fx.enums.OrderType;
import com.worldfirst.fx.model.Order;
import com.worldfirst.fx.repository.MatchRepository;
import com.worldfirst.fx.repository.OrderRepository;
import com.worldfirst.fx.util.MatchHandler;
import com.worldfirst.fx.util.UnMatchHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MatchHandler matchHandler;

    @Autowired
    private UnMatchHandler unMatchHandler;

    @Autowired
    private MatchRepository matchRepository;


    public CompletableFuture<Optional<Order>> create(Order order) {
       return orderRepository.create(order);
    }

    public CompletableFuture<Optional<Order>> findById(String id) {
        return orderRepository.findById(id);
    }

    public CompletableFuture<Collection<Order>> findAll() {
        return orderRepository.findAll();
    }

    public CompletableFuture<Optional<Order>> update(Order order) {
        return orderRepository.update(order);
    }

    public CompletableFuture<Boolean> delete(Order order) {
        return orderRepository.delete(order);
    }

    public List<Order> findByOrderTypeAndStatus(OrderType orderType, OrderStatus orderStatus){
        return orderRepository.findByOrderTypeAndStatus(orderType,orderStatus);
    }

    public CompletableFuture<Optional<Order>> cancelOrder(Order order){
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.update(order);

    }

    public CompletableFuture<Optional<Set<Map.Entry<String, List<Order>>>>> getMatchedOrders(){
        matchHandler.getMatchedOrders();
        return CompletableFuture.completedFuture(Optional.ofNullable(matchRepository.getAllMAtches()));
    }

    public CompletableFuture<Optional<List<Order>>> getUntMatchedOrders(){
        return unMatchHandler.getUnMatchedOrders();
    }
}
