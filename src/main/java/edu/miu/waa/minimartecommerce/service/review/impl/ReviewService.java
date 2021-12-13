package edu.miu.waa.minimartecommerce.service.review.impl;

import edu.miu.waa.minimartecommerce.domain.product.Product;
import edu.miu.waa.minimartecommerce.domain.review.Review;
import edu.miu.waa.minimartecommerce.domain.user.User;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.review.ReviewDto;
import edu.miu.waa.minimartecommerce.repository.review.ReviewRepository;
import edu.miu.waa.minimartecommerce.service.product.IProductService;
import edu.miu.waa.minimartecommerce.service.review.IReviewService;
import edu.miu.waa.minimartecommerce.service.user.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final IUserService userService;
    private final IProductService productService;

    public ReviewService(ReviewRepository reviewRepository, IUserService userService, IProductService productService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public List<Review> findAllByProductId(long productId) {
        return reviewRepository.findAllByProduct_IdAndAdminApproved(productId, true);
    }

    @Override
    public List<Review> findAllUnapprovedReview() {
        return reviewRepository.findAllByAdminApproved(false);
    }

    @Override
    public ResponseMessage save(ReviewDto dto) {
        Optional<User> userOpt = userService.findById(dto.getUserId());
        Optional<Product> productOpt = productService.findById(dto.getProductId());

        if(userOpt.isPresent() && productOpt.isPresent()){
            User user = userOpt.get();
            Product product = productOpt.get();

            reviewRepository.save(
                    new Review(dto.getComment(), false, user, product)
            );
            return new ResponseMessage("Saved Successfully.", HttpStatus.CREATED);
        }
        return new ResponseMessage("Data not found.", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseMessage update(ReviewDto dto) {
        Optional<Review> reviewOpt = reviewRepository.findById(dto.getReviewId());
        if(reviewOpt.isPresent()){
            Review review = reviewOpt.get();
            if(!dto.getComment().isEmpty()) review.setComment(dto.getComment());
            review.setAdminApproved(dto.isAdminApproved());

            reviewRepository.save(review);
            return new ResponseMessage("Review Updated.", HttpStatus.OK);
        }
        return new ResponseMessage("Review not found.", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseMessage delete(long id) {
        reviewRepository.deleteById(id);
        return new ResponseMessage("Deleted Review.", HttpStatus.OK);
    }
}
