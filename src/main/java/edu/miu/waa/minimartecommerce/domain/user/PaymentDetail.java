package edu.miu.waa.minimartecommerce.domain.user;

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
    private long id;

    @Column(name = "payment_type")
    private String paymentType; // master, or visa
    @Column(name = "card_no")
    private String cardNo;
    @Column(name = "security_code")
    private String securityCode;
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
