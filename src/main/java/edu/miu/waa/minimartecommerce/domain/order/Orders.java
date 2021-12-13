package edu.miu.waa.minimartecommerce.domain.order;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.user.ShippingAddress;
import edu.miu.waa.minimartecommerce.domain.user.User;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.OrderView.class, View.OrderListView.class, View.OrderAdminListView.class})
    private long id;

    @JsonView({View.OrderView.class, View.OrderListView.class,  View.OrderAdminListView.class})
    @Column(name = "created_date")
    private Date createdDate = new Date();

    @JsonView({View.OrderView.class})
    @Column(name = "updated_date")
    private Date updatedDate = new Date();

    @JsonView({View.OrderView.class, View.OrderAdminListView.class})
    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonView(View.OrderView.class)
    @OneToOne(targetEntity = ShippingAddress.class, cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id")
    private ShippingAddress shippingAddress;

    @JsonView({View.OrderView.class, View.OrderListView.class, View.OrderAdminListView.class})
    @ManyToOne(targetEntity = OrderStatus.class, cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_status_code")
    private OrderStatus orderStatus;

    @JsonView({View.OrderView.class, View.OrderListView.class, View.OrderAdminListView.class})
    @OneToMany(targetEntity = OrderItem.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private Set<OrderItem> orderItems = new HashSet<>();

    @JsonView({View.OrderView.class, View.OrderAdminListView.class})
    @OneToOne(targetEntity = Invoice.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    public Orders(User user, ShippingAddress shippingAddress, OrderStatus orderStatus, Set<OrderItem> orderItems, Invoice invoice){
        this.user = user;
        this.shippingAddress = shippingAddress;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
        this.invoice = invoice;
    }
}
