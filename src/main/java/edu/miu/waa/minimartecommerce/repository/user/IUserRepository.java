package edu.miu.waa.minimartecommerce.repository.user;

import edu.miu.waa.minimartecommerce.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    List<User> findAllByAdminApproved(boolean adminApproved);

    @Query("SELECT u FROM User u " +
            "INNER JOIN u.roles r " +
            "WHERE r.role = :role ")
    List<User> findAllByRoles(@Param("role") String role);
}
