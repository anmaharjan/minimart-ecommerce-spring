
package edu.miu.waa.minimartecommerce.domain.order;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "invoice_status")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InvoiceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.OrderView.class)
    private int id;

    @Column(name = "status")
    @JsonView(View.OrderView.class)
    private String status;
}
