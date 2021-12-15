package edu.miu.waa.minimartecommerce.controller.user;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.user.Followers;
import edu.miu.waa.minimartecommerce.domain.user.PaymentDetail;
import edu.miu.waa.minimartecommerce.domain.user.User;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.user.*;
import edu.miu.waa.minimartecommerce.service.user.IUserService;
import edu.miu.waa.minimartecommerce.view.View;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }

    @JsonView(View.UserListView.class)
    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @JsonView(View.UserDetailView.class)
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable(name = "id") long id){
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/seller")
    public ResponseEntity<ResponseMessage> saveSeller(@Valid @RequestBody UserDto dto){
        ResponseMessage response = userService.saveUsers(dto, true);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping("/buyer/save")
    public ResponseEntity<ResponseMessage> saveBuyer(@Valid @RequestBody UserDto dto){
        ResponseMessage response = userService.saveUsers(dto, false);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping("/info/update")
    public ResponseEntity<ResponseMessage> updateUserInfo(@RequestBody UpdateUserDto dto){
        ResponseMessage response = userService.updateInfo(dto);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping("/billing-address/update")
    public ResponseEntity<ResponseMessage> updateBillingAddress(@RequestBody UpdateAddressDto dto){
        ResponseMessage response = userService.updateBillingAddress(dto);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    /* ---- Admin ----- */
    @JsonView(View.UserListView.class)
    @GetMapping("/seller/unapproved/get-all")
    public ResponseEntity<List<User>> findAllUnapprovedSellers(){
        return ResponseEntity.ok(userService.getAllUnapprovedSellers());
    }

    @GetMapping("/seller/{id}/approve")
    public ResponseEntity<ResponseMessage> approveSeller(@PathVariable long id){
        ResponseMessage response = userService.approveSellers(id);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    /* ---- Buyer ---- */
    @PostMapping("/payment-details")
    public ResponseEntity<ResponseMessage> savePaymentDetail(@RequestBody PaymentDetailDto dto){
        ResponseMessage response = userService.addPaymentDetail(dto);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @JsonView(View.UserDetailView.class)
    @GetMapping("/{userId}/payment-details")
    public ResponseEntity<List<PaymentDetail>> findAllPaymentDetail(@PathVariable(name = "userId") long userId){
        return ResponseEntity.ok(userService.getAllPaymentDetails(userId));
    }

    /* ---- Followers ---- */
    @JsonView(View.UserListView.class)
    @GetMapping("/get-all/sellers")
    public ResponseEntity<List<User>> getAllSellers(){
        return ResponseEntity.ok(userService.findAllSellers());
    }

    @PostMapping("/following")
    public ResponseEntity<ResponseMessage> addFollowingUser(@RequestBody FollowersDto dto){
        ResponseMessage response = userService.addFollowingUser(dto);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping("/following/{followerId}")
    public ResponseEntity<ResponseMessage> removeFollowingUser(@PathVariable(name = "followerId") long followerId){
        ResponseMessage response = userService.removeFollowingUser(followerId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @JsonView(View.FollowerView.class)
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<Followers>> getAllFollowingByUserId(@PathVariable(name = "userId") long userId){
        return ResponseEntity.ok(userService.findAllByUserId(userId));
    }
}
