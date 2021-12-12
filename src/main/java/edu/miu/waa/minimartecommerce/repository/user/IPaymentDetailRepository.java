package edu.miu.waa.minimartecommerce.repository.user;

import edu.miu.waa.minimartecommerce.domain.user.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
    List<PaymentDetail> findAllByUser_Id(long userId);
}
