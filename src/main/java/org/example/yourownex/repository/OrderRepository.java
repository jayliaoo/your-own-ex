package org.example.yourownex.repository;

import org.aspectj.weaver.ast.Or;
import org.example.yourownex.entity.Order;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findFirstByStatusInAndType(Collection<String> statuses, String type, Sort sort);
}
