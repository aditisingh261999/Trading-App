package com.trade.coinwave.repository;

import com.trade.coinwave.model.Order;
import com.trade.coinwave.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrder(Order order);
}
