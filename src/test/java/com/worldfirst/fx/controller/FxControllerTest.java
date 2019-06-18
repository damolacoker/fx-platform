package com.worldfirst.fx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worldfirst.fx.FxApplication;
import com.worldfirst.fx.config.AsyncConfiguration;
import com.worldfirst.fx.enums.Currency;
import com.worldfirst.fx.enums.OrderStatus;
import com.worldfirst.fx.enums.OrderType;
import com.worldfirst.fx.model.Order;
import com.worldfirst.fx.repository.OrderRepository;
import com.worldfirst.fx.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = FxController.class)
@ContextConfiguration(classes={FxApplication.class, AsyncConfiguration.class})
public class FxControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;


    @Test
    public void createOrderTest() throws Exception {
        String userId = UUID.randomUUID().toString();
        Order testOrder = new Order("36552",userId,
                OrderType.BID, OrderStatus.PENDING, Currency.GBP_USD,
                1.2100, BigDecimal.ONE);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(testOrder);
        when(orderService.create(Mockito.any(Order.class)))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(testOrder)));
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/fx/register").content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200)).andReturn();
    }

    @Test
    public void createOrderFailureTest() throws Exception {
        String userId = UUID.randomUUID().toString();
        Order testOrder = null;
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(testOrder);
        when(orderService.create(Mockito.any(Order.class)))
                .thenReturn(CompletableFuture.completedFuture(Optional.ofNullable(testOrder)));
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/fx/register").content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();
    }

    @Test
    public void cancelOrderTest() throws Exception {
        String userId = UUID.randomUUID().toString();
        Order testOrder = new Order("36552",userId,
                OrderType.BID, OrderStatus.PENDING, Currency.GBP_USD,
                1.2100, BigDecimal.ONE);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(testOrder);
        when(orderService.cancelOrder(Mockito.any(Order.class)))
                .thenReturn(CompletableFuture.completedFuture(Optional.ofNullable(testOrder)));
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/fx/cancel").content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200)).andReturn();
    }

    @Test
    public void cancelOrderFailureTest() throws Exception {
        String userId = UUID.randomUUID().toString();
        Order testOrder = null;
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(testOrder);
        when(orderService.cancelOrder(Mockito.any(Order.class)))
                .thenReturn(CompletableFuture.completedFuture(Optional.ofNullable(testOrder)));
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/fx/cancel").content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();
    }

    @Test
    public void getMatchedOrders() throws Exception {
        when(orderService.getMatchedOrders())
                .thenReturn(CompletableFuture.completedFuture(Optional.ofNullable(matchedOrders())));
        mockMvc.perform(get("/api/fx/matched"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUnMatchedOrders() throws Exception {
        when(orderService.getUntMatchedOrders())
                .thenReturn(CompletableFuture.completedFuture(Optional.ofNullable(listOfUnMatchedOrders())));
        mockMvc.perform(get("/api/fx/unmatched"))
                .andExpect(status().isOk());
    }

    public Set<Map.Entry<String, List<Order>>> matchedOrders(){
        Map<String,List<Order>> matchedOrders = new HashMap<>();
        matchedOrders.put("12234", listOfMatchedOrders());
        return matchedOrders.entrySet();
    }

    private List<Order> listOfMatchedOrders(){
        List<Order> orders = new ArrayList<Order>();
        Order testOrder = new Order("36552", UUID.randomUUID().toString(),
                OrderType.BID, OrderStatus.PENDING, Currency.GBP_USD,
                1.2400, BigDecimal.ONE);

        Order testOrder1 = new Order("36553", UUID.randomUUID().toString(),
                OrderType.BID, OrderStatus.PENDING, Currency.GBP_USD,
                1.2300, BigDecimal.TEN);

        Order testOrder2 = new Order("36553", UUID.randomUUID().toString(),
                OrderType.BID, OrderStatus.PENDING, Currency.GBP_USD,
                1.2200, BigDecimal.TEN);

        orders.add(testOrder);
        orders.add(testOrder1);
        orders.add(testOrder2);
        return orders;
    }

    private List<Order> listOfUnMatchedOrders(){
        List<Order> orders = new ArrayList<Order>();
        Order testOrder = new Order("36552", UUID.randomUUID().toString(),
                OrderType.ASK, OrderStatus.PENDING, Currency.GBP_USD,
                1.51200, BigDecimal.ONE);

        Order testOrder1 = new Order("36553", UUID.randomUUID().toString(),
                OrderType.ASK, OrderStatus.PENDING, Currency.GBP_USD,
                1.31200, BigDecimal.TEN);

        Order testOrder2 = new Order("36553", UUID.randomUUID().toString(),
                OrderType.ASK, OrderStatus.PENDING, Currency.GBP_USD,
                1.41200, BigDecimal.TEN);

        orders.add(testOrder);
        orders.add(testOrder1);
        orders.add(testOrder2);
        return orders;
    }

}
