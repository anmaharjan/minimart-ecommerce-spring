package edu.miu.waa.minimartecommerce.domain.user;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;

import javax.persistence.*;

// For Buyer
@Entity
@Table(name = "followers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Followers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.FollowerView.class)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "following")
    @JsonView(View.FollowerView.class)
    private User following;

    public Followers(User user, User following){
        this.user = user;
        this.following = following;
    }
}
