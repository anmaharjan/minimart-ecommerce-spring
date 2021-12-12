package edu.miu.waa.minimartecommerce.dto.cart;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CartItemDto {
    private long productId;
    private long userId;
    private int quantity;
}
