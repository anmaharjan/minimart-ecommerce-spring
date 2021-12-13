package edu.miu.waa.minimartecommerce.dto.order;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InvoiceStatusDto {
    private long invoiceId;
    private String status;
}
