package edu.miu.waa.minimartecommerce.repository.order;

import edu.miu.waa.minimartecommerce.domain.order.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceStatusRepository extends JpaRepository<InvoiceStatus, Integer> {
    InvoiceStatus getByStatus(String status);
}
