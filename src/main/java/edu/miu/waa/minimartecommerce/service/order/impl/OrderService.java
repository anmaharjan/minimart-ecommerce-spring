package edu.miu.waa.minimartecommerce.service.order.impl;

import edu.miu.waa.minimartecommerce.domain.cart.CartItem;
import edu.miu.waa.minimartecommerce.domain.order.*;
import edu.miu.waa.minimartecommerce.domain.payment.Payment;
import edu.miu.waa.minimartecommerce.domain.user.PaymentDetail;
import edu.miu.waa.minimartecommerce.domain.user.ShippingAddress;
import edu.miu.waa.minimartecommerce.domain.user.User;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.order.OrderDto;
import edu.miu.waa.minimartecommerce.repository.order.InvoiceRepository;
import edu.miu.waa.minimartecommerce.repository.order.InvoiceStatusRepository;
import edu.miu.waa.minimartecommerce.repository.order.OrderRepository;
import edu.miu.waa.minimartecommerce.repository.order.OrderStatusRepository;
import edu.miu.waa.minimartecommerce.repository.payment.PaymentStatusRepository;
import edu.miu.waa.minimartecommerce.repository.user.IPaymentDetailRepository;
import edu.miu.waa.minimartecommerce.repository.user.IUserRepository;
import edu.miu.waa.minimartecommerce.service.cart.ICartItemService;
import edu.miu.waa.minimartecommerce.service.mail.IEmailService;
import edu.miu.waa.minimartecommerce.service.order.IOrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static edu.miu.waa.minimartecommerce.constant.product.InvoiceStatus.ISSUED;
import static edu.miu.waa.minimartecommerce.constant.product.OrderStatus.*;
import static edu.miu.waa.minimartecommerce.constant.product.PaymentStatus.PENDING;

@Service
public class OrderService implements IOrderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderRepository orderRepository;
    private final IUserRepository userRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final InvoiceStatusRepository invoiceStatusRepository;
    private final ICartItemService cartItemService;
    private final PaymentStatusRepository paymentStatusRepository;
    private final IPaymentDetailRepository paymentDetailRepository;
    private final ModelMapper modelMapper;
    private final InvoiceRepository invoiceRepository;
    private final IEmailService emailService;

    public OrderService(OrderRepository orderRepository, IUserRepository userRepository,
                        OrderStatusRepository orderStatusRepository,
                        InvoiceStatusRepository invoiceStatusRepository, ICartItemService cartItemService,
                        PaymentStatusRepository paymentStatusRepository,
                        IPaymentDetailRepository paymentDetailRepository, ModelMapper modelMapper, InvoiceRepository invoiceRepository, IEmailService emailService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.invoiceStatusRepository = invoiceStatusRepository;
        this.cartItemService = cartItemService;
        this.paymentStatusRepository = paymentStatusRepository;
        this.paymentDetailRepository = paymentDetailRepository;
        this.modelMapper = modelMapper;
        this.invoiceRepository = invoiceRepository;
        this.emailService = emailService;
    }

    @Override
    public Optional<Orders> findById(long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Orders> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Orders> findAllOrdersOfUser(long userId) {
        return orderRepository.findAllByUser_Id(userId);
    }

    @Override
    public ResponseMessage save(OrderDto orderDto) {
        Optional<User> userOpt = userRepository.findById(orderDto.getUserId());
        Optional<PaymentDetail> paymentDetailOpt = paymentDetailRepository.findById(orderDto.getPaymentDetailId());

        if(userOpt.isPresent() && paymentDetailOpt.isPresent()){
            User user = userOpt.get();

            List<CartItem> cartItems = cartItemService.getAllCartItemsByUserId(orderDto.getUserId());
            if(!cartItems.isEmpty()){
                Set<OrderItem> orderItems = new HashSet<>();

                cartItems.forEach(cartItem -> {
                    double itemPrice;
                    if(cartItem.getProduct().isOnSale()){
                        itemPrice = cartItem.getProduct().getSalePrice();
                    } else{
                        itemPrice = cartItem.getProduct().getActualPrice();
                    }
                    orderItems.add(new OrderItem(cartItem.getQuantity(), itemPrice, cartItem.getProduct()));
                });

                OrderStatus orderStatus = orderStatusRepository.getOneByStatus(NEW.toString());

                Payment payment = new Payment(0d,
                        paymentStatusRepository.getByStatus(PENDING.toString()));
                Invoice invoice = new Invoice(
                        invoiceStatusRepository.getByStatus(ISSUED.toString()),
                        payment, paymentDetailOpt.get());

                ShippingAddress address = modelMapper.map(orderDto.getShippingAddress(), ShippingAddress.class);
                address.setUser(user);

                orderRepository.save(
                        new Orders(
                                user, address, orderStatus, orderItems, invoice
                        )
                );
                cartItemService.deleteAllByUserId(user.getId());

                String subject = "Ordered Notification";
                String body = "<strong>Hi " + user.getFirstname() + "</strong>,<br/><br/>" +
                        "You have ordered product from MIU MiniMart. We will process it as soon as possible <br/><br/>" +
                        "Thank you<br/>" +
                        "<strong>MIU MiniMart</strong>";
                try {
                    emailService.sendMail(user.getUsername(), subject, body);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    logger.error("Error while sending email.");
                }

                return new ResponseMessage("Saved.", HttpStatus.OK);
            }
            return new ResponseMessage("Cart is empty.", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseMessage("Data not found.", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseMessage cancelOrderStatus(long id) {
        Optional<Orders> ordersOpt = orderRepository.findById(id);
        if (ordersOpt.isPresent()) {
            Orders orders = ordersOpt.get();

            String orderStatus = orders.getOrderStatus().getStatus();
            if (orderStatus.equalsIgnoreCase(NEW.toString())) {
                orders.setOrderStatus(orderStatusRepository.getOneByStatus(CANCELLED.toString()));
                orderRepository.save(orders);

                String subject = "Order Cancellation";
                String body = "<strong>Hi " + orders.getUser().getFirstname() + "</strong>,<br/><br/>" +
                        "Your order has been cancelled. <br/><br/>" +
                        "Thank you<br/>" +
                        "<strong>MIU MiniMart</strong>";
                try {
                    emailService.sendMail(orders.getUser().getUsername(), subject, body);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    logger.error("Error while sending email.");
                }

                return new ResponseMessage("Cancelled order.", HttpStatus.OK);
            }
            return new ResponseMessage("Cannot cancel the order.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseMessage("Order not found.", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseMessage updateOrderStatus(long id, String orderStatus) {
        Optional<Orders> ordersOpt = orderRepository.findById(id);
        OrderStatus status = orderStatusRepository.getOneByStatus(orderStatus.toUpperCase());
        if (ordersOpt.isPresent() && status !=null) {
            Orders orders = ordersOpt.get();
            orders.setOrderStatus(status);
            orderRepository.save(orders);

            String subject = "Order Status Update";
            String body = "<strong>Hi " + orders.getUser().getFirstname() + "</strong>,<br/><br/>" +
                    "Your order has been " + status.getStatus() + ". <br/><br/>" +
                    "Thank you<br/>" +
                    "<strong>MIU MiniMart</strong>";
            try {
                emailService.sendMail(orders.getUser().getUsername(), subject, body);
            } catch (MessagingException | UnsupportedEncodingException e) {
                logger.error("Error while sending email.");
            }

            return new ResponseMessage("Updated order status.", HttpStatus.OK);
        }
        return new ResponseMessage("Order not found.", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseMessage updateInvoiceStatus(long invoiceId, String invoiceStatus) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        InvoiceStatus status = invoiceStatusRepository.getByStatus(invoiceStatus.toUpperCase());

        if(invoiceOpt.isPresent() && status!=null){
            Invoice invoice = invoiceOpt.get();
            invoice.setInvoiceStatus(status);
            invoiceRepository.save(invoice);

            return new ResponseMessage("Updated invoice status.", HttpStatus.OK);
        }
        return new ResponseMessage("Invoice not found.", HttpStatus.NOT_FOUND);
    }

    @Override
    public Map<String, Integer> countAllCompletedOrderOfUser(long userId) {
        Map<String, Integer> count = new HashMap<>();
        count.put("count", orderRepository.countAllByOrderStatusAndUserId(userId, COMPLETED.toString()));
        return count;
    }
}
