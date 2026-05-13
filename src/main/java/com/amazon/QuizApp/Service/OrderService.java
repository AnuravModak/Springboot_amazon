package com.example.order;

import com.example.inventory.InventoryService;
import com.example.model.Order;
import com.example.model.Product;
import com.example.payment.PaymentService;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> placeOrder(Long userId, List<Long> productIds) {

        List<Order> orders = new ArrayList<>();
        double totalAmount = 0;

        if (productIds == null || productIds.isEmpty()) {
            return orders;
        }

        for (Long productId : productIds) {

            Product product = productRepository.findById(productId).orElse(null);

            if (product == null) {
                continue;
            }

            boolean reserved = inventoryService.reserveInventory(productId);

            if (!reserved) {
                continue;
            }

            totalAmount += product.getPrice();

            Order order = new Order();
            order.setUserId(userId);
            order.setProductIds(productIds);
            order.setTotalAmount(totalAmount);
            order.setCreatedAt(LocalDateTime.now());

            if (totalAmount > 20000) {
                order.setStatus("REVIEW");
            } else {
                order.setStatus("CREATED");
            }

            orders.add(order);
        }

        boolean isPaid = paymentService.processPayment(userId, totalAmount, "USD");

        if (isPaid) {
            for (Order order : orders) {
                orderRepository.save(order);
            }
        }

        return orders;
    }
}