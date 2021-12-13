package edu.miu.waa.minimartecommerce.dto.order;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderStatusDto {
    private long orderId;
    private String status;
}
