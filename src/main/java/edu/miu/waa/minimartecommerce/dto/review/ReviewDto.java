package edu.miu.waa.minimartecommerce.dto.review;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReviewDto {
    private long productId;
    private long reviewId;
    private String comment="";
    private long userId;
    private boolean adminApproved;

    public ReviewDto(long reviewId, boolean adminApproved){
        this.reviewId = reviewId;
        this.adminApproved = adminApproved;
    }
}
