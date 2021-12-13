package edu.miu.waa.minimartecommerce.domain.order;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.payment.Payment;
import edu.miu.waa.minimartecommerce.domain.user.PaymentDetail;
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

    @OneToOne(targetEntity = PaymentDetail.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_payment_detail_id")
    @JsonView({View.OrderView.class, View.OrderAdminListView.class})
    private PaymentDetail paymentDetail;

    @Column(name = "invoice_date")
    @JsonView(View.OrderView.class)
    private Date invoiceDate = new Date();

    public Invoice(InvoiceStatus invoiceStatus, Payment payment, PaymentDetail paymentDetail){
        this.invoiceStatus = invoiceStatus;
        this.payment = payment;
        this.paymentDetail = paymentDetail;
        this.invoiceDate = new Date();
    }
}
