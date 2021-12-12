package edu.miu.waa.minimartecommerce.domain.order;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.payment.Payment;
import edu.miu.waa.minimartecommerce.domain.payment.PaymentMethod;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invoices")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.OrderView.class)
    private long id;

    @OneToOne(targetEntity = InvoiceStatus.class, cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_status_code")
    @JsonView(View.OrderView.class)
    private InvoiceStatus invoiceStatus;

    @OneToOne(targetEntity = Payment.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @JsonView({View.OrderView.class, View.OrderAdminListView.class})
    private Payment payment;

    @ManyToOne(targetEntity = PaymentMethod.class, cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    @JsonView(View.OrderView.class)
    private PaymentMethod paymentMethod;

    @Column(name = "invoice_date")
    @JsonView(View.OrderView.class)
    private Date invoiceDate;
}
