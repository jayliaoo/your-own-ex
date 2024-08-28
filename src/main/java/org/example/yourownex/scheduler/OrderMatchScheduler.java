package org.example.yourownex.scheduler;

import org.example.yourownex.service.*;
import org.springframework.stereotype.*;

@Component
public class OrderMatchScheduler {

    private final OrderService orderService;

    public OrderMatchScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

//    @Scheduled(fixedDelay = 0)
    public void orderMatch() {
        orderService.match();
    }
}
