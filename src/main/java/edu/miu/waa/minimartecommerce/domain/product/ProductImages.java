package edu.miu.waa.minimartecommerce.domain.product;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.view.View;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductImages{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.ProductView.class})
    private long id;

    @JsonView({View.ProductView.class, View.ProductListView.class})
    private String name;
    @JsonView(View.ProductView.class)
    private String extension;
    @Column(name = "image_path", columnDefinition = "text")
    private String imagePath;
    @JsonView({View.ProductView.class, View.ProductListView.class, View.CartView.class, View.OrderView.class})
    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    public ProductImages(String name, String extension,
                         String imagePath, String imageUrl) {
        this.name = name;
        this.extension = extension;
        this.imagePath = imagePath;
        this.imageUrl = imageUrl;
    }
}
