package com.worldfirst.fx.util;

import com.worldfirst.fx.enums.OrderStatus;
import com.worldfirst.fx.enums.OrderType;
import com.worldfirst.fx.model.Order;
import com.worldfirst.fx.repository.MatchRepository;
import com.worldfirst.fx.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MatchHandler {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Value( "${fx.current.price:1.2100}" )
    private Double currentPrice;

    public List<Order> getMatchedOrders(){
        List<Order> matchedOrders = null;
        List<Order> pendingBidOrders = orderRepository.findByOrderTypeAndStatus(OrderType.BID,OrderStatus.PENDING);
        List<Order> pendingAskOrders = orderRepository.findByOrderTypeAndStatus(OrderType.ASK,OrderStatus.PENDING);
        for (Order bidOrder : pendingBidOrders) {
            matchedOrders = pendingAskOrders.stream().filter(askOrder ->
                    askOrder.getPrice() <= currentPrice && askOrder.getPrice() <= bidOrder.getPrice()
                            && askOrder.getAmount().compareTo(bidOrder.getAmount()) <= 0
            ).collect(Collectors.toList());
            matchRepository.addMatchedOrders(bidOrder.getId(),matchedOrders);
        }
        return matchedOrders;
    }
}
