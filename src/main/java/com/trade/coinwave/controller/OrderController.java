package com.trade.coinwave.controller;

import com.trade.coinwave.domain.OrderType;
import com.trade.coinwave.model.Coin;
import com.trade.coinwave.model.Order;
import com.trade.coinwave.model.User;
import com.trade.coinwave.request.CreateOrderRequest;
import com.trade.coinwave.service.CoinService;
import com.trade.coinwave.service.OrderService;
import com.trade.coinwave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private WalletTransactionService walletTransactionService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateOrderRequest createOrderRequest
            ) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        Coin coin = coinService.findById(createOrderRequest.getCoinId());

        Order order = orderService.processOrder(
                coin,
                createOrderRequest.getQuantity(),
                createOrderRequest.getOrderType(),
                user
        );
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById (
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable Long orderId
            ) throws Exception {

        User user = userService.findUserProfileByJwtToken(jwtToken);
        Order order = orderService.getOrderById(orderId);

        if(order.getUser().getId().equals(orderId)) {
            return ResponseEntity.ok(order);
        } else {
            throw new Exception("Invalid order id");
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders (
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(required = false) OrderType orderType,
            @RequestParam(required = false) String asset_symbol
    ) throws Exception {

        Long userId = userService.findUserProfileByJwtToken(jwtToken).getId();
        List<Order> orders = orderService.getOrdersByUser(
                userId,
                orderType,
                asset_symbol
        );
        return ResponseEntity.ok(orders);
    }
}
