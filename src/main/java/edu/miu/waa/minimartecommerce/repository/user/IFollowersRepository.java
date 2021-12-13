package edu.miu.waa.minimartecommerce.repository.user;

import edu.miu.waa.minimartecommerce.domain.user.Followers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFollowersRepository extends JpaRepository<Followers, Long> {
    List<Followers> findAllByUser_Id(long id);

    boolean existsByUser_IdAndFollowing_Id(long userId, long followingId);
}
