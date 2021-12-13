package edu.miu.waa.minimartecommerce.controller.order;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.order.Orders;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.order.InvoiceStatusDto;
import edu.miu.waa.minimartecommerce.dto.order.OrderDto;
import edu.miu.waa.minimartecommerce.dto.order.OrderStatusDto;
import edu.miu.waa.minimartecommerce.service.order.IOrderService;
import edu.miu.waa.minimartecommerce.view.View;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @JsonView(View.OrderListView.class)
    @GetMapping
    public ResponseEntity<List<Orders>> findAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @JsonView(View.OrderListView.class)
    @GetMapping("/user/{buyerId}")
    public ResponseEntity<List<Orders>> findAllByUserId(@PathVariable(name = "buyerId") long buyerId){
        return ResponseEntity.ok(orderService.findAllOrdersOfUser(buyerId));
    }

    @JsonView(View.OrderView.class)
    @GetMapping("/{id}")
    public ResponseEntity<Orders> findById(@PathVariable(name = "id") long id){
        Optional<Orders> orders = orderService.findById(id);
        return orders.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/checkout")
    public ResponseEntity<ResponseMessage> checkout(@RequestBody OrderDto dto){
        ResponseMessage response = orderService.save(dto);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping("/{orderId}/cancel")
    public ResponseEntity<ResponseMessage> cancelOrder(@PathVariable(name = "orderId") long orderId){
        ResponseMessage response = orderService.cancelOrderStatus(orderId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping("/order-status/update")
    public ResponseEntity<ResponseMessage> updateOrderStatus(@RequestBody OrderStatusDto dto){
        ResponseMessage response = orderService.updateOrderStatus(dto.getOrderId(), dto.getStatus());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping("/invoice-status/update")
    public ResponseEntity<ResponseMessage> updateInvoiceStatus(@RequestBody InvoiceStatusDto dto){
        ResponseMessage response = orderService.updateInvoiceStatus(dto.getInvoiceId(), dto.getStatus());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
