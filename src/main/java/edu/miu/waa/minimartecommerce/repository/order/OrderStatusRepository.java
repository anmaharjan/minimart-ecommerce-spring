package edu.miu.waa.minimartecommerce.repository.order;

import edu.miu.waa.minimartecommerce.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    OrderStatus getOneByStatus(String status);

    List<OrderStatus> findAllByOrderByStatus();
}
