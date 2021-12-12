package edu.miu.waa.minimartecommerce.repository.order;

import edu.miu.waa.minimartecommerce.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
}
