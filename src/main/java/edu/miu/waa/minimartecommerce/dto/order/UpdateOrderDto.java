package edu.miu.waa.minimartecommerce.dto.order;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateOrderDto{
    private String orderId = "";
    private String orderStatus = "";
    private String paymentStatus = "";
    private double paidAmount = 0;
}
