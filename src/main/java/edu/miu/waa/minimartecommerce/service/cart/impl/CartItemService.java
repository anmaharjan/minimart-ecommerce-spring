package edu.miu.waa.minimartecommerce.service.cart.impl;

import edu.miu.waa.minimartecommerce.domain.cart.CartItem;
import edu.miu.waa.minimartecommerce.domain.product.Product;
import edu.miu.waa.minimartecommerce.domain.user.User;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.cart.CartItemDto;
import edu.miu.waa.minimartecommerce.dto.cart.CartItemUpdateDto;
import edu.miu.waa.minimartecommerce.repository.cart.ICartItemRepository;
import edu.miu.waa.minimartecommerce.service.cart.ICartItemService;
import edu.miu.waa.minimartecommerce.service.product.IProductService;
import edu.miu.waa.minimartecommerce.service.user.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartItemService implements ICartItemService {
    private final ICartItemRepository cartItemRepository;
    private final IUserService userService;
    private final IProductService productService;
    private final ModelMapper modelMapper;

    public CartItemService(ICartItemRepository cartItemRepository, IUserService userService, IProductService productService, ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CartItem> getAllCartItemsByUserId(long userId) {
        return cartItemRepository.findAllByUser_Id(userId);
    }

    @Override
    public ResponseMessage add(CartItemDto dto) {
        Optional<CartItem> cartItemOpt = cartItemRepository
                .findByUser_IdAndProduct_Id(dto.getUserId(), dto.getProductId());

        if(cartItemOpt.isPresent()){
            CartItem cartItem = cartItemOpt.get();
            if(dto.getQuantity() > cartItem.getProduct().getStockQuantity())
                return new ResponseMessage("Out of Stock.", HttpStatus.BAD_REQUEST);

            cartItem.setQuantity(dto.getQuantity());
            cartItemRepository.save(cartItem);
            return new ResponseMessage("Saved.", HttpStatus.CREATED);
        }
        else{
            Optional<Product> product = productService.findById(dto.getProductId());
            Optional<User> user = userService.findById(dto.getUserId());

            if(product.isPresent() && user.isPresent()){
                Product product1 = product.get();
                if(dto.getQuantity() > product1.getStockQuantity())
                    return new ResponseMessage("Out of Stock.", HttpStatus.BAD_REQUEST);

                CartItem cartItem = new CartItem(product1, dto.getQuantity(), user.get());
                cartItemRepository.save(cartItem);
                return new ResponseMessage("Saved.", HttpStatus.CREATED);
            }
            else
                return new ResponseMessage("Data not found!!", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseMessage update(CartItemUpdateDto dto) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(dto.getId());

        if(cartItemOpt.isPresent()){
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(dto.getQuantity());
            cartItemRepository.save(cartItem);
            return new ResponseMessage("Updated.", HttpStatus.OK);
        }
        else
            return new ResponseMessage("Data not found!!", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseMessage deleteById(long id) {
        cartItemRepository.deleteById(id);
        return new ResponseMessage("Deleted.", HttpStatus.OK);
    }

    @Override
    public Map<String, Integer> countCartItemsByUserId(long userId) {
        Map<String, Integer> count = new HashMap<>();
        count.put("cart", cartItemRepository.countAllByUser_Id(userId));
        return count;
    }

    @Override
    public void deleteAllByUserId(long userId) {
        cartItemRepository.deleteAllByUser_Id(userId);
    }
}
