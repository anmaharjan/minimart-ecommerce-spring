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
    @JsonView({View.CommentView.class})
    private long id;

    @Column(columnDefinition = "text")
    @JsonView({View.CommentView.class})
    private String comment;
    @JsonView({View.CommentView.class})
    private int rate;
    @Column(name = "created_date")
    @JsonView({View.CommentView.class})
    private Date createdDate;
    @Column(name = "updated_date")
    @JsonView({View.CommentView.class})
    private Date updatedDate;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonView({View.CommentView.class})
    private User user;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
