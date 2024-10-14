package com.trade.coinwave.repository;

import com.trade.coinwave.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    PaymentOrder findByOrderId(Long orderId);
}
