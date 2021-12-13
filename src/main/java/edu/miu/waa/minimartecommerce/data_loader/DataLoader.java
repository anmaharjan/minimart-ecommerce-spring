package edu.miu.waa.minimartecommerce.data_loader;

import edu.miu.waa.minimartecommerce.domain.order.InvoiceStatus;
import edu.miu.waa.minimartecommerce.domain.order.OrderStatus;
import edu.miu.waa.minimartecommerce.domain.payment.PaymentStatus;
import edu.miu.waa.minimartecommerce.domain.user.Role;
import edu.miu.waa.minimartecommerce.domain.user.User;
import edu.miu.waa.minimartecommerce.repository.order.InvoiceStatusRepository;
import edu.miu.waa.minimartecommerce.repository.order.OrderStatusRepository;
import edu.miu.waa.minimartecommerce.repository.payment.PaymentStatusRepository;
import edu.miu.waa.minimartecommerce.repository.user.IRoleRepository;
import edu.miu.waa.minimartecommerce.repository.user.IUserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.miu.waa.minimartecommerce.constant.product.InvoiceStatus.ISSUED;
import static edu.miu.waa.minimartecommerce.constant.product.InvoiceStatus.PAID;
import static edu.miu.waa.minimartecommerce.constant.product.OrderStatus.*;
import static edu.miu.waa.minimartecommerce.constant.user.Role.*;

@Component
public class DataLoader implements ApplicationRunner {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InvoiceStatusRepository invoiceStatusRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final PaymentStatusRepository paymentStatusRepository;

    public DataLoader(IRoleRepository roleRepository, IUserRepository userRepository,
                      PasswordEncoder passwordEncoder, InvoiceStatusRepository invoiceStatusRepository,
                      OrderStatusRepository orderStatusRepository, 
                      PaymentStatusRepository paymentStatusRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.invoiceStatusRepository = invoiceStatusRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.paymentStatusRepository = paymentStatusRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(
                    Arrays.asList(
                        new Role(ADMIN.toString()),
                        new Role(SELLER.toString()),
                        new Role(BUYER.toString())
                    )
                );
        }

        if(userRepository.count() == 0){
            Set<Role> roles = Stream.of(roleRepository.findByRole(ADMIN.toString()))
                    .collect(Collectors.toSet());
            String password = passwordEncoder.encode("Qwerty12345");
            User user = new User("Admin", "", "MiniMart",
                    "admin@minimart.com", password, roles);
            user.setAdminApproved(true);
            user.setActive(true);
            userRepository.save(user);
        }

        if(invoiceStatusRepository.count() == 0){
            invoiceStatusRepository.saveAll(Arrays.asList(
                    new InvoiceStatus(ISSUED.toString()),
                    new InvoiceStatus(PAID.toString())
            ));
        }

        if(orderStatusRepository.count() == 0){
            orderStatusRepository.saveAll(Arrays.asList(
                    new OrderStatus(COMPLETED.toString()),
                    new OrderStatus(CANCELLED.toString()),
                    new OrderStatus(NEW.toString()),
                    new OrderStatus(SHIPPED.toString())
            ));
        }

        if(paymentStatusRepository.count() == 0){
            paymentStatusRepository.saveAll(Arrays.asList(
                    new PaymentStatus(PAID.toString()),
                    new PaymentStatus(PENDING.toString()),
                    new PaymentStatus(CANCELLED.toString()),
                    new PaymentStatus(REFUNDED.toString())
            ));
        }
    }
}
