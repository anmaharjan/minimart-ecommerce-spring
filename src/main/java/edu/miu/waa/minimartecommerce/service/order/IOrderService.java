package edu.miu.waa.minimartecommerce.service.order;

import edu.miu.waa.minimartecommerce.domain.order.Orders;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.order.OrderDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IOrderService {
    Optional<Orders> findById(long id);

    List<Orders> findAll();

    List<Orders> findAllOrdersOfUser(long userId);

    ResponseMessage save(OrderDto orderDto);

    ResponseMessage cancelOrderStatus(long id);

    ResponseMessage updateOrderStatus(long id, String orderStatus);

    ResponseMessage updateInvoiceStatus(long invoiceId, String invoiceStatus);

    Map<String, Integer> countAllCompletedOrderOfUser(long userId);
}
