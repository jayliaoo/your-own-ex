package org.example.yourownex.service;

import lombok.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class OrderMatchingEngine {
    private final TreeMap<Long, LinkedHashMap<Long, Order>> sellOrders = new TreeMap<>();
    private final TreeMap<Long, LinkedHashMap<Long, Order>> buyOrders = new TreeMap<>();

    public void insertAndMatch(Order order) {
        if (order.getSide().equals("BUY")) {
            if (sellOrders.floorEntry(order.getPrice()) != null) {
                //TODO DO matching and execution
            } else {
                buyOrders.computeIfAbsent(order.getPrice(), k -> new LinkedHashMap<>())
                        .put(order.getId(), order);
            }
        } else {
            if (buyOrders.ceilingEntry(order.getPrice()) != null) {
                //TODO DO matching and execution
            } else {
                sellOrders.computeIfAbsent(order.getPrice(), k -> new LinkedHashMap<>())
                        .put(order.getId(), order);
            }
        }
    }

    public void cancel(Order order) {
        if (order.getSide().equals("BUY")) {
            LinkedHashMap<Long, Order> orders = buyOrders.get(order.getPrice());
            orders.remove(order.getId());
            if (orders.isEmpty()) {
                buyOrders.remove(order.getPrice());
            }
        } else {
            LinkedHashMap<Long, Order> orders = sellOrders.get(order.getPrice());
            orders.remove(order.getId());
            if (orders.isEmpty()) {
                sellOrders.remove(order.getPrice());
            }
        }
    }
}

@Data
class Order {
    private Long id;
    private String side;
    private Long price;
}