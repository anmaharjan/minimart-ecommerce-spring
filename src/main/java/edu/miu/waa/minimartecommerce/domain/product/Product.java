package edu.miu.waa.minimartecommerce.domain.product;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.cart.CartItem;
import edu.miu.waa.minimartecommerce.domain.review.Review;
import edu.miu.waa.minimartecommerce.domain.user.User;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.ProductView.class, View.ProductListView.class, View.CartView.class, View.OrderView.class, View.ReviewView.class})
    private long id;

    @JsonView({View.ProductView.class, View.ProductListView.class, View.CartView.class, View.OrderView.class, View.ReviewView.class})
    private String name;
    @JsonView({View.ProductView.class, View.ProductListView.class, View.CartView.class, View.OrderView.class})
    @Column(name = "actual_price")
    private double actualPrice;
    @JsonView({View.ProductView.class, View.ProductListView.class, View.CartView.class, View.OrderView.class})
    @Column(name = "sale_price")
    private double salePrice = 0;
    @JsonView({View.ProductView.class, View.ProductListView.class, View.CartView.class, View.OrderView.class})
    @Column(name = "on_sale")
    private boolean onSale;
    @JsonView({View.ProductView.class, View.ProductListView.class})
    @Column(name = "quantity_in_stock")
    private int stockQuantity;

    @JsonView({View.ProductView.class})
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @JsonView({View.ProductView.class, View.ProductEditView.class})
    @Column(columnDefinition = "text")
    private String description = "";
    @JsonView({View.ProductView.class, View.ProductListView.class, View.ProductEditView.class, View.OrderView.class, View.CartView.class})
    @Column(columnDefinition = "text")
    private String highlights = "";

    @JsonView({View.ProductView.class, View.ProductListView.class, View.CartView.class, View.OrderView.class, View.ReviewView.class})
    @OneToMany(targetEntity = ProductImages.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @Fetch(FetchMode.SELECT)
    private List<ProductImages> productImages = new ArrayList<>();

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.SELECT)
    private User user;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "product", fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "product", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
}
