package edu.miu.waa.minimartecommerce.repository.user;

import edu.miu.waa.minimartecommerce.domain.user.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBillingAddressRepository extends JpaRepository<BillingAddress, Long> {
}
