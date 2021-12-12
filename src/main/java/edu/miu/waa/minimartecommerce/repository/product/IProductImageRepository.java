package edu.miu.waa.minimartecommerce.repository.product;

import edu.miu.waa.minimartecommerce.domain.product.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductImageRepository extends JpaRepository<ProductImages, Long> {
}
