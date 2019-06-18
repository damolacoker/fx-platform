package com.worldfirst.fx.util;

import com.worldfirst.fx.enums.Currency;
import com.worldfirst.fx.enums.OrderStatus;
import com.worldfirst.fx.enums.OrderType;
import com.worldfirst.fx.model.Order;
import com.worldfirst.fx.repository.MatchRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MatchHandlerTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private MatchHandler matchHandler;

    @Mock
    private MatchRepository matchRepository;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(matchHandler, "currentPrice", 1.2100);
    }

    @Test
    public void getMatchedOrdersTest(){
        when(orderRepository.findByOrderTypeAndStatus(OrderType.BID,OrderStatus.PENDING)).thenReturn(listOfPendingBidOrders());
        when(orderRepository.findByOrderTypeAndStatus(OrderType.ASK,OrderStatus.PENDING)).thenReturn(listOfPendingAskOrders());
        List<Order> listOfMatchedOrders = matchHandler.getMatchedOrders();
        assertEquals(2,listOfMatchedOrders.size());

    }

    private List<Order> listOfPendingBidOrders(){
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

    private List<Order> listOfPendingAskOrders(){
        List<Order> orders = new ArrayList<Order>();
        Order testOrder = new Order("36552", UUID.randomUUID().toString(),
                OrderType.ASK, OrderStatus.PENDING, Currency.GBP_USD,
                1.11200, BigDecimal.ONE);

        Order testOrder1 = new Order("36553", UUID.randomUUID().toString(),
                OrderType.ASK, OrderStatus.PENDING, Currency.GBP_USD,
                1.11200, BigDecimal.TEN);

        Order testOrder2 = new Order("36553", UUID.randomUUID().toString(),
                OrderType.ASK, OrderStatus.PENDING, Currency.GBP_USD,
                1.31200, BigDecimal.TEN);

        orders.add(testOrder);
        orders.add(testOrder1);
        orders.add(testOrder2);
        return orders;
    }
}
