package edu.miu.waa.minimartecommerce.domain.review;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.product.Product;
import edu.miu.waa.minimartecommerce.domain.user.User;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.ReviewView.class)
    private long id;

    @Column(columnDefinition = "text")
    @JsonView(View.ReviewView.class)
    private String comment;
    @Column(name = "created_date")
    @JsonView(View.ReviewView.class)
    private Date createdDate = new Date();
    @Column(name = "updated_date")
    @JsonView(View.ReviewView.class)
    private Date updatedDate = new Date();

    @Column(name = "admin_approved")
    private boolean adminApproved;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonView(View.ReviewView.class)
    private User user;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonView(View.ReviewView.class)
    private Product product;

    public Review(String comment, boolean adminApproved, User user, Product product){
        this.comment = comment;
        this.adminApproved = adminApproved;
        this.user = user;
        this.product = product;
    }
}
