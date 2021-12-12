package edu.miu.waa.minimartecommerce.controller.cart;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.cart.CartItem;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.cart.CartItemDto;
import edu.miu.waa.minimartecommerce.dto.cart.CartItemUpdateDto;
import edu.miu.waa.minimartecommerce.service.cart.ICartItemService;
import edu.miu.waa.minimartecommerce.view.View;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final ICartItemService cartItemService;

    public CartController(ICartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @JsonView(View.CartView.class)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItem>> findAllCartItemsByUserId(@PathVariable(name = "userId") long userId){
        return ResponseEntity.ok(cartItemService.getAllCartItemsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> save(@RequestBody CartItemDto dto){
        ResponseMessage response = cartItemService.add(dto);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping
    public ResponseEntity<ResponseMessage> update(@RequestBody CartItemUpdateDto dto){
        ResponseMessage response = cartItemService.update(dto);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping("{cartId}/user/{userId}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable(name = "cartId") long cartId,
                                                  @PathVariable(name = "userId") long userId){
        ResponseMessage response = cartItemService.deleteByIdAndUserId(cartId, userId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Map<String, Integer>> count(@PathVariable(name = "userId") long userId){
        return ResponseEntity.ok(cartItemService.countCartItemsByUserId(userId));
    }
}
