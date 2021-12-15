package edu.miu.waa.minimartecommerce.controller.product;

import com.fasterxml.jackson.annotation.JsonView;
import edu.miu.waa.minimartecommerce.domain.product.Product;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.product.ProductRequestDto;
import edu.miu.waa.minimartecommerce.service.product.IProductService;
import edu.miu.waa.minimartecommerce.view.View;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    /*@PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseMessage> save(@RequestPart("images") MultipartFile[] images,
                                                @RequestPart("product")ProductRequestDto product){
        ResponseMessage response = productService.save(images, product);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }*/

    @PostMapping
    public ResponseEntity<ResponseMessage> save(@RequestBody ProductRequestDto product){
        ResponseMessage response = productService.save(null, product);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping
    public ResponseEntity<ResponseMessage> update(@RequestBody ProductRequestDto dto){
        ResponseMessage response = productService.update(dto);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping("{productId}/upload/images")
    public ResponseEntity<ResponseMessage> uploadImages(@PathVariable(name = "productId") long productId,
                                                        @RequestParam(name = "images") MultipartFile[] images){
        ResponseMessage response = productService.uploadImages(productId, images);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping
    @JsonView(View.ProductListView.class)
    public ResponseEntity<List<Product>> findAll(){
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/user/{id}/all")
    @JsonView(View.ProductListView.class)
    public ResponseEntity<List<Product>> findAllByUserId(@PathVariable long id){
        return ResponseEntity.ok(productService.findAllByUserId(id));
    }

    @GetMapping("/{id}")
    @JsonView(View.ProductView.class)
    public ResponseEntity<Product> findById(@PathVariable(name = "id") long id){
        Optional<Product> productOpt = productService.findById(id);
        return productOpt.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteById(@PathVariable(name = "id") long id){
        ResponseMessage response = productService.deleteById(id);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
