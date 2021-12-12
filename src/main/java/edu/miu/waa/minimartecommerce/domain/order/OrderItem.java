package edu.miu.waa.minimartecommerce.domain.order;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.product.Product;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.OrderView.class)
    private long id;

    @JsonView({View.OrderView.class, View.OrderListView.class, View.OrderAdminListView.class})
    private int quantity;
    @JsonView({View.OrderView.class, View.OrderListView.class, View.OrderAdminListView.class})
    private double itemPrice;

    @OneToOne(targetEntity = Product.class, cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonView(View.OrderView.class)
    private Product product;
}
