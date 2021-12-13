package edu.miu.waa.minimartecommerce.domain.user;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "shipping_address")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.OrderView.class)
    private long id;

    @NotEmpty
    @Column(columnDefinition = "text")
    @JsonView(View.OrderView.class)
    private String address ="";
    @NotEmpty
    @JsonView(View.OrderView.class)
    private String city="";
    @NotEmpty
    @JsonView(View.OrderView.class)
    private String state="";
    @NotEmpty
    @Column(name = "contact_no")
    @JsonView(View.OrderView.class)
    private String contactNo="";

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
