package edu.miu.waa.minimartecommerce.dto.cart;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CartItemUpdateDto {
    private long id;
    private int quantity;
}
