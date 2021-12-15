package edu.miu.waa.minimartecommerce.repository.order;

import edu.miu.waa.minimartecommerce.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUser_Id(long id);

    @Query("SELECT COUNT(ord) FROM Orders ord " +
            "INNER JOIN ord.orderItems oi " +
            "INNER JOIN oi.product p " +
            "WHERE p.id=:id")
    int countAllByProductId(@Param(value = "id") long id);

    @Query("SELECT count(ord) FROM Orders ord " +
            "INNER JOIN ord.orderStatus st " +
            "INNER JOIN ord.user us " +
            "WHERE us.id=:userId AND st.status=:orderStatus")
    int countAllByOrderStatusAndUserId(@Param(value="userId") long userId,
                                       @Param(value="orderStatus") String status);
}
