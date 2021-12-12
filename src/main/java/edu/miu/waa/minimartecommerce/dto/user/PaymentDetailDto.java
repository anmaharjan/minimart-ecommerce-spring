package edu.miu.waa.minimartecommerce.dto.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaymentDetailDto {
    private String paymentType; // master, or visa
    private String cardNo;
    private String securityCode;
    private String expiry;

    private long userId;
}
