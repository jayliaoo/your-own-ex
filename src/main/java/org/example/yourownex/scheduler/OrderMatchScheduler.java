package org.example.yourownex.scheduler;

import org.example.yourownex.entity.Order;
import org.example.yourownex.repository.OrderRepository;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMatchScheduler {
    public static final List<String> STATUSES = List.of("init", "partially_fulfilled");

    private final OrderRepository orderRepository;

    public OrderMatchScheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedDelay = 0)
    public void orderMatch() {
        Order buyOrder = orderRepository.findFirstByStatusInAndType(STATUSES, "buy",
                Sort.by(Sort.Order.desc("price"), Sort.Order.asc("id")));
        if (buyOrder == null) {
            return;
        }
        Order sellOrder = orderRepository.findFirstByStatusInAndType(STATUSES, "sell",
                Sort.by(Sort.Order.asc("price"), Sort.Order.asc("id")));
        if (sellOrder == null) {
            return;
        }
        if (buyOrder.getPrice().compareTo(sellOrder.getPrice()) >= 0) {
            int compareTo = buyOrder.getAmountLeft().compareTo(sellOrder.getAmountLeft());
            if (compareTo > 0) {
                buyOrder.setAmountLeft(buyOrder.getAmountLeft().subtract(sellOrder.getAmountLeft()));
                buyOrder.setStatus("partially_fulfilled");

                sellOrder.setAmountLeft(BigDecimal.ZERO);
                sellOrder.setStatus("fulfilled");
            } else if (compareTo < 0) {
                buyOrder.setAmountLeft(BigDecimal.ZERO);
                buyOrder.setStatus("fulfilled");

                sellOrder.setAmountLeft(sellOrder.getAmountLeft().subtract(buyOrder.getAmountLeft()));
                sellOrder.setStatus("partially_fulfilled");
            } else {
                buyOrder.setAmountLeft(BigDecimal.ZERO);
                buyOrder.setStatus("fulfilled");

                sellOrder.setAmountLeft(BigDecimal.ZERO);
                sellOrder.setStatus("fulfilled");
            }
            orderRepository.save(buyOrder);
            orderRepository.save(sellOrder);
        }
    }
}
