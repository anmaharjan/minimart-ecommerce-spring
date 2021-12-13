package edu.miu.waa.minimartecommerce.controller.review;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.review.Review;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.review.ReviewDto;
import edu.miu.waa.minimartecommerce.service.review.IReviewService;
import edu.miu.waa.minimartecommerce.view.View;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @JsonView(View.ReviewView.class)
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> findAllByProductId(@PathVariable(name = "productId") long productId){
        return ResponseEntity.ok(reviewService.findAllByProductId(productId));
    }

    @JsonView(View.ReviewView.class)
    @GetMapping("/unapproved")
    public ResponseEntity<List<Review>> findAllUnapprovedReview(){
        return ResponseEntity.ok(reviewService.findAllUnapprovedReview());
    }

    @GetMapping("/approve/{id}")
    public ResponseEntity<ResponseMessage> approveReview(@PathVariable(name = "id") long id){
        ResponseMessage response = reviewService.update(new ReviewDto(id, true));
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> save(@RequestBody ReviewDto dto){
        ResponseMessage response = reviewService.save(dto);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping
    public ResponseEntity<ResponseMessage> update(@RequestBody ReviewDto dto){
        ResponseMessage response = reviewService.update(dto);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable(name = "reviewId") long reviewId){
        ResponseMessage response = reviewService.delete(reviewId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
