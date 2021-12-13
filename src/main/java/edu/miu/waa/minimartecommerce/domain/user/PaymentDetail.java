package edu.miu.waa.minimartecommerce.domain.user;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_payment_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.UserDetailView.class, View.OrderView.class})
    private long id;

    @JsonView({View.UserDetailView.class, View.OrderView.class})
    @Column(name = "payment_type")
    private String paymentType; // master, or visa
    @JsonView({View.UserDetailView.class, View.OrderView.class})
    @Column(name = "card_no")
    private String cardNo;
    @JsonView(View.UserDetailView.class)
    @Column(name = "security_code")
    private String securityCode;
    @JsonView(View.UserDetailView.class)
    @Column(name = "expiry")
    private String expiry;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public PaymentDetail(String paymentType, String cardNo, String securityCode, String expiry){
        this.paymentType = paymentType;
        this.cardNo = cardNo;
        this.securityCode = securityCode;
        this.expiry = expiry;
    }
}
