package edu.miu.waa.minimartecommerce.service.product;

import edu.miu.waa.minimartecommerce.domain.product.Product;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.product.ProductRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    ResponseMessage save(MultipartFile[] images, ProductRequestDto dto);

    ResponseMessage update(ProductRequestDto dto);

    ResponseMessage uploadImages(long id, MultipartFile[] images);

    List<Product> findAll();

    List<Product> findAllByUserId(long id);

    Optional<Product> findById(long id);

    ResponseMessage deleteById(long id);

    ResponseMessage deleteImagesByImageId(long id);

}
