package com.worldfirst.fx.model;


import com.worldfirst.fx.enums.Currency;
import com.worldfirst.fx.enums.OrderStatus;
import com.worldfirst.fx.enums.OrderType;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Order {
    private String id;
    private String userId;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Currency currency;
    private Double price;
    private BigDecimal amount;

}
