package edu.miu.waa.minimartecommerce.domain.payment;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payment_methods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.OrderView.class)
    private int id;

    @JsonView(View.OrderView.class)
    @Column(name = "method")
    private String method;
}
