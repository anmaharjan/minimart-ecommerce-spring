package edu.miu.waa.minimartecommerce.repository.payment;

import edu.miu.waa.minimartecommerce.domain.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {
    PaymentStatus getByStatus(String status);

    List<PaymentStatus> findAllByOrderByStatus();
}
