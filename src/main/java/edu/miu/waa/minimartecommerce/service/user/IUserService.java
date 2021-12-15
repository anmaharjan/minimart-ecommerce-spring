package edu.miu.waa.minimartecommerce.service.user;

import edu.miu.waa.minimartecommerce.domain.user.Followers;
import edu.miu.waa.minimartecommerce.domain.user.PaymentDetail;
import edu.miu.waa.minimartecommerce.domain.user.User;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.user.*;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAll();

    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);

    ResponseMessage saveUsers(UserDto userDto, boolean seller);

    ResponseMessage updateInfo(UpdateUserDto dto);

    ResponseMessage updateBillingAddress(UpdateAddressDto dto);

    List<User> getAllUnapprovedSellers();

    ResponseMessage approveSellers(long id);

    ResponseMessage addPaymentDetail(PaymentDetailDto dto);

    List<PaymentDetail> getAllPaymentDetails(long userId);

    /* ---------- Followers ------------ */
    List<User> findAllSellers();

    ResponseMessage addFollowingUser(FollowersDto dto);

    ResponseMessage removeFollowingUser(long followersId);

    List<Followers> findAllByUserId(long id);
}
