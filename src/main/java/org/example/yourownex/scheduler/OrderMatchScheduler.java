//package org.example.yourownex.scheduler;
//
//import org.example.yourownex.entity.Order;
//import org.example.yourownex.repository.OrderRepository;
//import org.springframework.data.domain.Limit;
//import org.springframework.data.domain.Sort;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.springframework.data.domain.Sort.Order.asc;
//import static org.springframework.data.domain.Sort.Order.desc;
//
//@Component
//public class OrderMatchScheduler {
//    public static final List<String> STATUSES = List.of("init", "partially_fulfilled");
//
//    private final OrderRepository orderRepository;
//
//    public OrderMatchScheduler(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }
//
//    @Scheduled(fixedDelay = 0)
//    public void orderMatch() {
//        List<Order> buyOrders = orderRepository.findByStatusInAndType(STATUSES, "buy",
//                Sort.by(desc("price"), asc("id")), Limit.of(1));
//        if (buyOrders.isEmpty()) {
//            return;
//        }
//        Order buyOrder = buyOrders.getFirst();
//        List<Order> sellOrders = orderRepository.findByStatusInAndType(STATUSES, "sell",
//                Sort.by(asc("price"), asc("id")), Limit.of(10));
//        if (sellOrders.isEmpty()) {
//            return;
//        }
//        for (Order sellOrder : sellOrders) {
//            if (buyOrder.getPrice().compareTo(sellOrder.getPrice()) >= 0) {
//                int compareTo = buyOrder.getAmountLeft().compareTo(sellOrder.getAmountLeft());
//                if (compareTo > 0) {
//                    buyOrder.setAmountLeft(buyOrder.getAmountLeft().subtract(sellOrder.getAmountLeft()));
//                    buyOrder.setStatus("partially_fulfilled");
//
//                    sellOrder.setAmountLeft(BigDecimal.ZERO);
//                    sellOrder.setStatus("fulfilled");
//                } else if (compareTo < 0) {
//                    buyOrder.setAmountLeft(BigDecimal.ZERO);
//                    buyOrder.setStatus("fulfilled");
//
//                    sellOrder.setAmountLeft(sellOrder.getAmountLeft().subtract(buyOrder.getAmountLeft()));
//                    sellOrder.setStatus("partially_fulfilled");
//                } else {
//                    buyOrder.setAmountLeft(BigDecimal.ZERO);
//                    buyOrder.setStatus("fulfilled");
//
//                    sellOrder.setAmountLeft(BigDecimal.ZERO);
//                    sellOrder.setStatus("fulfilled");
//                }
//                orderRepository.save(buyOrder);
//                orderRepository.save(sellOrder);
//            }
//        }
//    }
//}
