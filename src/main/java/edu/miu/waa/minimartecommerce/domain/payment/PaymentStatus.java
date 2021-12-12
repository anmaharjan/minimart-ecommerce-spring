package edu.miu.waa.minimartecommerce.domain.payment;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payment_status")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.OrderView.class)
    private int id;

    @JsonView({View.OrderView.class, View.OrderAdminListView.class})
    @Column(name = "status")
    private String status;

    public PaymentStatus(String status){
        this.status = status;
    }
}
