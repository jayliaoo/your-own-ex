package org.example.yourownex.scheduler;

import org.example.yourownex.dao.OrderService;
import org.example.yourownex.jooq.tables.records.OrderRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMatchScheduler {

    private final OrderService orderService;

    public OrderMatchScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

//    @Scheduled(fixedDelay = 0)
    public void orderMatch() {
        OrderRecord buyOrder = orderService.findBuyOrder();
        if (buyOrder == null) {
            return;
        }
        List<OrderRecord> sellOrders = orderService.findSellOrders(buyOrder.getPrice());
        if (sellOrders.isEmpty()) {
            return;
        }
        for (OrderRecord sellOrder : sellOrders) {
            if (buyOrder.getAmountLeft().equals(BigDecimal.ZERO)) {
                break;
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
                orderService.save(buyOrder);
                orderService.save(sellOrder);
            }
        }
    }
}
