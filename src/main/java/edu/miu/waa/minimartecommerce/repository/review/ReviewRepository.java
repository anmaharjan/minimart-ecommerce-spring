package edu.miu.waa.minimartecommerce.repository.review;

import edu.miu.waa.minimartecommerce.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByAdminApproved(boolean adminApproved);

    List<Review> findAllByProduct_IdAndAdminApproved(long id, boolean adminApproved);
}
