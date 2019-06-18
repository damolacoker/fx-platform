package com.worldfirst.fx.repository;

import com.worldfirst.fx.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MatchRepository {
    private Map<String, List<Order>> matchedOrderMap = new ConcurrentHashMap<String, List<Order>>();

    public void addMatchedOrders(String id, List<Order> orders){

        matchedOrderMap.put(id,orders);
    }

    public Set<Map.Entry<String, List<Order>>> getAllMAtches(){
        return matchedOrderMap.entrySet();
    }
}
