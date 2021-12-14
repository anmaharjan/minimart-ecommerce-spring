package edu.miu.waa.minimartecommerce.service.cart;

import edu.miu.waa.minimartecommerce.domain.cart.CartItem;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.cart.CartItemDto;
import edu.miu.waa.minimartecommerce.dto.cart.CartItemUpdateDto;

import java.util.List;
import java.util.Map;

public interface ICartItemService {
    List<CartItem> getAllCartItemsByUserId(long userId);

    ResponseMessage add(CartItemDto dto);

    ResponseMessage update(CartItemUpdateDto dto);

    ResponseMessage deleteById(long id);

    Map<String, Integer> countCartItemsByUserId(long userId);

    void deleteAllByUserId(long userId);
}
