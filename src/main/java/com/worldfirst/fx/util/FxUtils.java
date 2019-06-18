package com.worldfirst.fx.util;

import com.worldfirst.fx.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Slf4j
public class FxUtils {

    public static Function<Optional<Set<Map.Entry<String, List<Order>>>>, ResponseEntity> matchedOrdersResponse = matchedOrders -> matchedOrders
            .<ResponseEntity>map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());

    public static Function<Optional<List<Order>>, ResponseEntity> unMatchedOrdersResponse = unMatchedOrders -> unMatchedOrders
            .<ResponseEntity>map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());


    public static Function<Optional<Order>, ResponseEntity> orderResponse = orders -> orders
            .<ResponseEntity>map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());

    public static Function<Throwable, ResponseEntity> handleGetOrdersFailure = throwable -> {
        log.error("Unable to get orders", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

    public static Function<String, Function<Throwable, ResponseEntity>> handleGetOrderFailure = orderID -> throwable -> {
        log.error(String.format("Unable to retrieve order for id: %s", orderID), throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

    public static Function<Order, Function<Throwable, ResponseEntity>> handleCancelOrderFailure = order -> throwable -> {
        log.error(String.format("Unable to retrieve order for id: %s", order.getId()), throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

}
