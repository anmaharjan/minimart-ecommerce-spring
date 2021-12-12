package edu.miu.waa.minimartecommerce.domain.order;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_status")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.OrderView.class)
    private int id;

    @Column(name = "status")
    @JsonView({View.OrderView.class, View.OrderListView.class, View.OrderAdminListView.class})
    private String status;

    public OrderStatus(String status){
        this.status = status;
    }
}
