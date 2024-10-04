package com.trade.coinwave.service;

import com.trade.coinwave.domain.OrderType;
import com.trade.coinwave.model.Coin;
import com.trade.coinwave.model.Order;
import com.trade.coinwave.model.OrderItem;
import com.trade.coinwave.model.User;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);
    Order getOrderById(Long orderId) throws Exception;
    List<Order> getOrdersByUser(Long userId, OrderType orderType, String symbol);
    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;
}
