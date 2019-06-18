package com.worldfirst.fx.util;

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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UnMatchHandlerTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UnMatchHandler unMatchHandler;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(unMatchHandler, "currentPrice", 1.2100);
    }

    @Test
    public void getUnMatchedOrdersTest() throws ExecutionException, InterruptedException {
        when(orderRepository.findByOrderTypeAndStatus(OrderType.ASK,OrderStatus.PENDING)).thenReturn(listOfPendingAskOrders());
        List<Order> listOfUnMatchedOrders = unMatchHandler.getUnMatchedOrders().get().get();
        assertEquals(3,listOfUnMatchedOrders.size());
    }

    private List<Order> listOfPendingAskOrders(){
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
