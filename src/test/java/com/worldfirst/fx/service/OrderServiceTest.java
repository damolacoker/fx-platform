package com.worldfirst.fx.service;

import com.worldfirst.fx.enums.Currency;
import com.worldfirst.fx.enums.OrderStatus;
import com.worldfirst.fx.enums.OrderType;
import com.worldfirst.fx.model.Order;
import com.worldfirst.fx.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;


    @InjectMocks
    private OrderService orderService;


    @Test
    public void createOrderTest() throws ExecutionException, InterruptedException {
        String expectedId = "36552";
        String userId = UUID.randomUUID().toString();
        Order testOrder = new Order("36552",userId,
                OrderType.ASK, OrderStatus.PENDING, Currency.GBP_USD,
                1.2100, BigDecimal.ONE);
        when(orderRepository.create(Mockito.any(Order.class)))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(testOrder)));
        CompletableFuture<Optional<Order>> expectedOrder = orderService.create(testOrder);
        assertEquals(expectedId,expectedOrder.get().get().getId());
        assertNotEquals(OrderType.BID,expectedOrder.get().get().getOrderType());

    }

    @Test
    public void updateOrderTest() throws ExecutionException, InterruptedException {
        String expectedId = "36552";
        String userId = UUID.randomUUID().toString();
        Order testOrder = new Order("36552",userId,
                OrderType.ASK, OrderStatus.PENDING, Currency.GBP_USD,
                1.2100, BigDecimal.ONE);
        when(orderRepository.create(Mockito.any(Order.class)))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(testOrder)));
        when(orderRepository.update(Mockito.any(Order.class)))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(testOrder)));
        CompletableFuture<Optional<Order>> beforeUpadteExpectedOrder = orderService.create(testOrder);

        assertEquals(BigDecimal.ONE,beforeUpadteExpectedOrder.get().get().getAmount());

        testOrder.setAmount(BigDecimal.TEN);
        CompletableFuture<Optional<Order>> expectedOrder = orderService.update(testOrder);

        assertEquals(BigDecimal.TEN,expectedOrder.get().get().getAmount());

    }

    @Test
    public void findByIdOrderTest() throws ExecutionException, InterruptedException {
        String userId = UUID.randomUUID().toString();
        Order testOrder = new Order("36552",userId,
                OrderType.BID, OrderStatus.PENDING, Currency.GBP_USD,
                1.2100, BigDecimal.ONE);
        when(orderRepository.create(testOrder))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(testOrder)));
        Order testOrder1 = new Order("36553",userId,
                OrderType.ASK, OrderStatus.PENDING, Currency.GBP_USD,
                1.2100, BigDecimal.TEN);
        when(orderRepository.create(testOrder1))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(testOrder1)));
        Order testOrder2 = new Order("36554",userId,
                OrderType.BID, OrderStatus.PENDING, Currency.GBP_USD, 1.2100, BigDecimal.ONE);
        when(orderRepository.create(testOrder2))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(testOrder2)));

        when(orderRepository.findById("36553")).thenReturn(CompletableFuture.completedFuture(Optional.of(testOrder1)));
        CompletableFuture<Optional<Order>> expectedOrder = orderService.findById("36553");
        assertTrue(expectedOrder.get().isPresent());

    }

    @Test
    public void finAllOrdersTest() throws ExecutionException, InterruptedException {
        when(orderRepository.findAll()).thenReturn(CompletableFuture.completedFuture(listOfOrders()));
        CompletableFuture<Collection<Order>> orders = orderService.findAll();
        assertEquals(3,orders.get().size());

    }

    @Test
    public void deleteOrder() throws ExecutionException, InterruptedException {
        String userId = UUID.randomUUID().toString();
        Order testOrder = new Order("36552",userId,
                OrderType.BID, OrderStatus.PENDING, Currency.GBP_USD,
                1.2100, BigDecimal.ONE);
        when(orderRepository.delete(testOrder))
                .thenReturn(CompletableFuture.completedFuture(Boolean.TRUE));
        CompletableFuture<Boolean> deletedOrder = orderService.delete(testOrder);
        assertTrue(deletedOrder.get());
    }

    @Test
    public void cancelOrderTest() throws ExecutionException, InterruptedException {
        String userId = UUID.randomUUID().toString();
        Order testOrder = new Order("36552",userId,
                OrderType.BID, OrderStatus.PENDING, Currency.GBP_USD,
                1.2100, BigDecimal.ONE);
        when(orderRepository.update(testOrder))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(testOrder)));
        CompletableFuture<Optional<Order>> expectedOrder = orderService.cancelOrder(testOrder);
        assertEquals(expectedOrder.get().get().getOrderStatus(),OrderStatus.CANCELLED);
    }

    private Collection<Order> listOfOrders(){
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
}
