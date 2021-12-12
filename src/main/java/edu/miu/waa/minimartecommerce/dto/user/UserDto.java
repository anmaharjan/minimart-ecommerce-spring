package edu.miu.waa.minimartecommerce.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    @NotEmpty
    private String firstname;
    private String middlename;
    @NotEmpty
    private String lastname;
    @NotNull
    @NotEmpty
    @Email
    private String username;
    @NotNull
    @NotEmpty
    private String password;

    @Valid
    private AddressDto billingAddress = null;
}
