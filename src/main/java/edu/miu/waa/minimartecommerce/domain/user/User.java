package edu.miu.waa.minimartecommerce.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.order.OrderItem;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.UserListView.class,  View.UserDetailView.class})
    private long id;

    @NotNull
    @NotEmpty
    @JsonView({View.OrderView.class, View.ReviewView.class, View.UserListView.class,
            View.UserDetailView.class, View.OrderAdminListView.class, View.FollowerView.class})
    private String firstname;
    @JsonView({View.OrderView.class, View.ReviewView.class, View.UserListView.class,
            View.UserDetailView.class, View.OrderAdminListView.class, View.FollowerView.class})
    private String middlename;
    @NotNull
    @NotEmpty
    @JsonView({View.OrderView.class, View.ReviewView.class, View.UserListView.class,
            View.UserDetailView.class, View.OrderAdminListView.class, View.FollowerView.class})
    private String lastname;

    @NotNull
    @NotEmpty
    @Email
    @JsonView({View.OrderView.class, View.ReviewView.class, View.UserListView.class,
            View.UserDetailView.class, View.OrderAdminListView.class, View.FollowerView.class})
    private String username;

    @NotNull
    @NotEmpty
    private String password;

    @Column(name = "created_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    @JsonView({View.UserListView.class, View.UserDetailView.class})
    private Date createdDate = new Date();

    private boolean active;
    @Column(name = "admin_approved")
    private boolean adminApproved;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name="users_role",
            joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "id"))
    @JsonView({View.UserListView.class, View.UserDetailView.class})
    private Set<Role> roles;

    @JsonView({View.OrderView.class, View.UserDetailView.class})
    @OneToOne(targetEntity = BillingAddress.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id")
    private BillingAddress billingAddress;

    @OneToMany(targetEntity = ShippingAddress.class, mappedBy = "user")
    private List<ShippingAddress> shippingAddress;

    @OneToMany(targetEntity = PaymentDetail.class, cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "user")
    private List<PaymentDetail> paymentDetails = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Followers> followers = new ArrayList<>();

    public User(String firstname, String middlename, String lastname, String username,
                String password, Set<Role> roles){
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
