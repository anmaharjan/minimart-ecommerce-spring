package edu.miu.waa.minimartecommerce.repository.review;

import edu.miu.waa.minimartecommerce.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByAdminApproved(boolean adminApproved);

    List<Review> findAllByProduct_IdAndAdminApproved(long id, boolean adminApproved);
}
