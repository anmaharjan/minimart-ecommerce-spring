
package edu.miu.waa.minimartecommerce.repository.order;

import edu.miu.waa.minimartecommerce.domain.order.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
