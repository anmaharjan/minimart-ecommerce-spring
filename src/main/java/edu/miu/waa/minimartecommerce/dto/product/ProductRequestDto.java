package edu.miu.waa.minimartecommerce.dto.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductRequestDto {
    private long id;
    private String name="";
    private double actualPrice =0;
    private double salePrice=0;
    private int stockQuantity;
    private String description = "";
    private String highlights = "";

    private long userId;
}
