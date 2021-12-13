package edu.miu.waa.minimartecommerce.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.order.Invoice;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonView(View.OrderView.class)
    @Column(name = "payment_date")
    private Date paymentDate = new Date();

    @JsonView(View.OrderView.class)
    @Column(name = "paid_amount")
    private double paidAmount;

    @OneToOne(targetEntity = PaymentStatus.class, cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_status_id")
    @JsonView({View.OrderView.class, View.OrderAdminListView.class})
    private PaymentStatus paymentStatus;

    @JsonIgnore
    @OneToOne(targetEntity = Invoice.class, cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    public Payment(double paidAmount, PaymentStatus paymentStatus){
        this.paidAmount = paidAmount;
        this.paymentStatus = paymentStatus;
    }
}
