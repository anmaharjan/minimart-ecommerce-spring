package edu.miu.waa.minimartecommerce.repository.cart;

import edu.miu.waa.minimartecommerce.domain.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByUser_Id(long id);

    Optional<CartItem> findByUser_IdAndProduct_Id(long userId, long productId);

    @Transactional
    void deleteByIdAndUser_Id(long id, long userId);

    int countAllByUser_Id(long userId);

    @Transactional
    void deleteAllByUser_Id(long userId);

    @Transactional
    void deleteAllByProduct_Id(long productId);
}
