package com.trade.coinwave.service;

import com.trade.coinwave.model.Order;

public interface OrderService {

    Order getOrderById(Long orderId);
}
