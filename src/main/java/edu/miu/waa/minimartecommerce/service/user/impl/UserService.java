package edu.miu.waa.minimartecommerce.service.user.impl;

import edu.miu.waa.minimartecommerce.domain.user.*;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.user.*;
import edu.miu.waa.minimartecommerce.repository.user.*;
import edu.miu.waa.minimartecommerce.service.user.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.miu.waa.minimartecommerce.constant.user.Role.BUYER;
import static edu.miu.waa.minimartecommerce.constant.user.Role.SELLER;

@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final IPaymentDetailRepository paymentDetailRepository;
    private final IFollowersRepository followersRepository;
    private final IBillingAddressRepository billingAddressRepository;

    public UserService(IUserRepository userRepository, IRoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, IPaymentDetailRepository paymentDetailRepository, IFollowersRepository followersRepository, IBillingAddressRepository billingAddressRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.paymentDetailRepository = paymentDetailRepository;
        this.followersRepository = followersRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public ResponseMessage saveUsers(UserDto userDto, boolean seller) {
        if(!userRepository.existsByUsernameIgnoreCase(userDto.getUsername())){
            User user = modelMapper.map(userDto, User.class);
            Role role;
            if(seller){
                role = roleRepository.findByRole("SELLER");
                user.setAdminApproved(false);
            }
            else{
                role = roleRepository.findByRole("BUYER");
                user.setAdminApproved(true);
            }
            user.setActive(true);
            user.setRoles(Stream.of(role).collect(Collectors.toSet()));
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);

            return new ResponseMessage("Saved Successfully.", HttpStatus.CREATED);
        }
        return new ResponseMessage(
                String.format("Duplicate user!! User with %s already exists.", userDto.getUsername()),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseMessage updateInfo(UpdateUserDto dto) {
        Optional<User> userOpt = userRepository.findById(dto.getId());
        if(userOpt.isPresent()){
            User user = userOpt.get();
            if(!dto.getFirstname().isEmpty()) user.setFirstname(dto.getFirstname());
            if(!dto.getMiddlename().isEmpty()) user.setMiddlename(dto.getMiddlename());
            if(!dto.getLastname().isEmpty()) user.setLastname(dto.getLastname());
            userRepository.save(user);
            return new ResponseMessage("Updated Successfully.", HttpStatus.OK);
        }
        return new ResponseMessage("User not found!!", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseMessage updateBillingAddress(UpdateAddressDto dto) {
        Optional<BillingAddress> billingAddressOpt = billingAddressRepository.findById(dto.getId());
        if(billingAddressOpt.isPresent()){
            BillingAddress billingAddress = billingAddressOpt.get();
            if(!dto.getAddress().isEmpty()) billingAddress.setAddress(dto.getAddress());
            if(!dto.getCity().isEmpty()) billingAddress.setCity(dto.getCity());
            if(!dto.getContactNo().isEmpty()) billingAddress.setContactNo(dto.getContactNo());
            if(!dto.getState().isEmpty()) billingAddress.setState(dto.getState());
            billingAddressRepository.save(billingAddress);
            return new ResponseMessage("Updated Successfully.", HttpStatus.OK);
        }
        return new ResponseMessage("Billing Address not found!!", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> getAllUnapprovedSellers() {
        return userRepository.findAllByAdminApproved(false);
    }

    @Override
    public ResponseMessage approveSellers(long id) {
        Optional<User> userOpt = findById(id);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setAdminApproved(true);
            userRepository.save(user);
            return new ResponseMessage(String.format("%s %s has been approved to be seller", user.getFirstname(), user.getLastname()),
                    HttpStatus.OK);
        }
        return new ResponseMessage("User not found!!", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseMessage addPaymentDetail(PaymentDetailDto dto) {
        Optional<User> userOpt = findById(dto.getUserId());
        if(userOpt.isPresent()){
            PaymentDetail paymentDetail = new PaymentDetail(dto.getPaymentType(), dto.getCardNo(),
                    dto.getSecurityCode(), dto.getExpiry(), userOpt.get());
            paymentDetail.setUser(userOpt.get());

            paymentDetailRepository.save(paymentDetail);
            return new ResponseMessage("Saved.", HttpStatus.CREATED);
        }
        return new ResponseMessage("User not found.", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<PaymentDetail> getAllPaymentDetails(long userId) {
        return paymentDetailRepository.findAllByUser_Id(userId);
    }

    @Override
    public List<User> findAllSellers() {
        return userRepository.findAllByRoles(SELLER.toString());
    }

    /* ---------- Followers ------------ */
    @Override
    public ResponseMessage addFollowingUser(FollowersDto dto) {
        if(followersRepository.existsByUser_IdAndFollowing_Id(dto.getUserId(), dto.getFollowingId())){
            return new ResponseMessage("Added following.", HttpStatus.OK);
        }
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        Optional<User> followingOpt = userRepository.findById(dto.getFollowingId());

        if(userOpt.isPresent() && followingOpt.isPresent()){
            User user = userOpt.get();
            User following = followingOpt.get();
            boolean isBuyer = user.getRoles()
                    .stream().map(Role::getRole)
                    .anyMatch(name -> name.equalsIgnoreCase(BUYER.toString()));

            boolean isSeller = following.getRoles()
                    .stream().map(Role::getRole)
                    .anyMatch(name -> name.equalsIgnoreCase(SELLER.toString()));
            if(isBuyer && isSeller){
                followersRepository.save(new Followers(user, followingOpt.get()));
                return new ResponseMessage("Added following.", HttpStatus.OK);
            }
            else{
                return new ResponseMessage("You should be buyer and the other person must be seller",
                        HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseMessage("Data not found.", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseMessage removeFollowingUser(long followersId) {
        followersRepository.deleteById(followersId);
        return new ResponseMessage("Deleted.", HttpStatus.OK);
    }

    @Override
    public List<Followers> findAllByUserId(long id) {
        return followersRepository.findAllByUser_Id(id);
    }
}
