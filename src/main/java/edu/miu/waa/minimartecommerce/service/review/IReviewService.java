package edu.miu.waa.minimartecommerce.service.review;

import edu.miu.waa.minimartecommerce.domain.review.Review;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.review.ReviewDto;

import java.util.List;

public interface IReviewService {
    List<Review> findAllByProductId(long productId);

    List<Review> findAllUnapprovedReview();

    ResponseMessage save(ReviewDto dto);

    ResponseMessage update(ReviewDto dto);

    ResponseMessage delete(long id);
}
