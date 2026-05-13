package com.amazon.QuizApp.Service;

import com.amazon.QuizApp.Entity.Inventory;
import com.amazon.QuizApp.Entity.Order;
import com.amazon.QuizApp.Entity.Product;
import com.amazon.QuizApp.Repositories.InventoryRepository;
import com.amazon.QuizApp.Repositories.OrderRepository;
import com.amazon.QuizApp.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentService paymentService;

    public List<Order> placeOrder(Long userId, List<Long> productIds) {

        List<Order> orders = new ArrayList<>();

        double total = 0;

        for (Long id : productIds) {

            Product product = productRepository.findById(id).orElse(null);

            if (product == null) {
                continue;
            }

            Inventory inventory = inventoryRepository.findById(id).orElse(null);

            if (inventory != null && inventory.getAvailableQuantity() > 0) {

                inventory.setAvailableQuantity(inventory.getAvailableQuantity() - 1);

                total += product.getPrice();

                Order order = new Order();
                order.setUserId(userId);
                order.setProductIds(productIds);
                order.setTotalAmount(total);
                order.setCreatedAt(LocalDateTime.now());
                order.setStatus("CREATED");

                orders.add(order);
            }
        }

        paymentService.processPayment(userId, total, "INR");

        for (Order order : orders) {
            orderRepository.save(order);
        }

        return orders;
    }
}