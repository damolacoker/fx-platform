package com.worldfirst.fx.controller;

import com.worldfirst.fx.config.AsyncConfiguration;
import com.worldfirst.fx.model.Order;
import com.worldfirst.fx.service.OrderService;
import com.worldfirst.fx.util.FxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(value = FxController.REQUEST_PATH_API_FX_PLATFORM)
public class FxController {

    static final String REQUEST_PATH_API_FX_PLATFORM = "/api/fx";

    @Autowired
    private OrderService orderService;

    @Async(AsyncConfiguration.TASK_EXECUTOR_CONTROLLER)
    @PostMapping(value = "/register",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseEntity> registerOrder(@RequestBody Order order) {
        return orderService.create(order)
                .thenApply(FxUtils.orderResponse)
                .exceptionally(FxUtils.handleGetOrdersFailure);
    }

    @Async(AsyncConfiguration.TASK_EXECUTOR_CONTROLLER)
    @PutMapping(value = "/cancel",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseEntity> cancelOrder(@RequestBody Order order) {
        return orderService.cancelOrder(order).
                thenApply(FxUtils.orderResponse)
                .exceptionally(FxUtils.handleGetOrdersFailure);
    }

    @Async(AsyncConfiguration.TASK_EXECUTOR_CONTROLLER)
    @GetMapping(value = "/matched",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseEntity> getMatchedOrders() {
        return orderService.getMatchedOrders()
                .thenApply(FxUtils.matchedOrdersResponse)
                .exceptionally(FxUtils.handleGetOrdersFailure);
    }

    @Async(AsyncConfiguration.TASK_EXECUTOR_CONTROLLER)
    @GetMapping(value = "/unmatched",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseEntity> getUnMatchedOrders() {
        return orderService.getUntMatchedOrders()
                .thenApply(FxUtils.unMatchedOrdersResponse)
                .exceptionally(FxUtils.handleGetOrdersFailure);
    }

}
