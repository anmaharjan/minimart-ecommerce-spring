package edu.miu.waa.minimartecommerce.dto.order;

import edu.miu.waa.minimartecommerce.domain.user.ShippingAddress;
import edu.miu.waa.minimartecommerce.dto.user.AddressDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDto{
    private long orderId;
    private long userId;
    private long paymentDetailId;
    private AddressDto shippingAddress;
}
