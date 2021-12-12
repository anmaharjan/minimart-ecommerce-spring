package edu.miu.waa.minimartecommerce.service.product.impl;

import edu.miu.waa.minimartecommerce.domain.product.Product;
import edu.miu.waa.minimartecommerce.domain.product.ProductImages;
import edu.miu.waa.minimartecommerce.domain.user.User;
import edu.miu.waa.minimartecommerce.dto.ResponseMessage;
import edu.miu.waa.minimartecommerce.dto.product.ProductRequestDto;
import edu.miu.waa.minimartecommerce.exceptionHandling.exceptions.ProductException;
import edu.miu.waa.minimartecommerce.repository.product.IProductImageRepository;
import edu.miu.waa.minimartecommerce.repository.product.IProductRepository;
import edu.miu.waa.minimartecommerce.service.product.IProductService;
import edu.miu.waa.minimartecommerce.service.user.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final IUserService userService;
    private final IProductImageRepository productImageRepository;

    public ProductService(IProductRepository productRepository, ModelMapper modelMapper, IUserService userService, IProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public ResponseMessage save(MultipartFile[] images, ProductRequestDto dto) {
        Optional<User> userOpt = userService.findById(dto.getUserId());

        if(userOpt.isPresent()){
            User user = userOpt.get();

            if(!user.isAdminApproved())
                return new ResponseMessage(String.format("%s is not authorized to upload product.",
                        user.getUsername()), HttpStatus.METHOD_NOT_ALLOWED);

            Product product = modelMapper.map(dto, Product.class);
            product.setCreatedDate(new Date());
            product.setUpdatedDate(new Date());

            String folderRelativeLocation = "/images/products/";
            String folderLocation = System.getProperty("user.dir") + folderRelativeLocation;
            File imageFolder = new File(folderLocation);
            if (!imageFolder.exists())
                imageFolder.mkdirs();

            List<ProductImages> productImages = new ArrayList<>();
            boolean success = saveImages(productImages, images, folderLocation, folderRelativeLocation);

            if(success){
                product.setProductImages(productImages);
                productRepository.save(product);
                return new ResponseMessage("Product have been saved successfully.", HttpStatus.CREATED);
            }
            return new ResponseMessage("Extension doesn't match.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseMessage("User not found!!", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseMessage update(ProductRequestDto dto) {
        Optional<Product> productOpt = productRepository.findById(dto.getId());
        Optional<User> userOpt = userService.findById(dto.getUserId());
        if(productOpt.isPresent() && userOpt.isPresent()){
            Product product = productOpt.get();

            if(product.getUser().getId() != dto.getUserId())
                return new ResponseMessage("Unauthorized attempt to change data.", HttpStatus.METHOD_NOT_ALLOWED);

            if(!dto.getName().isEmpty()) product.setName(dto.getName());
            if(dto.getActualPrice()!=0) product.setActualPrice(dto.getActualPrice());
            if(dto.getSalePrice()!=0) product.setSalePrice(dto.getSalePrice());
            if(dto.getStockQuantity()!=0) product.setStockQuantity(dto.getStockQuantity());
            if(!dto.getHighlights().isEmpty()) product.setHighlights(dto.getHighlights());
            if(!dto.getDescription().isEmpty()) product.setDescription(dto.getDescription());
            product.setUpdatedDate(new Date());

            productRepository.save(product);
            return new ResponseMessage("Updated Successfully.", HttpStatus.OK);
        }
        return new ResponseMessage("Bad request", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseMessage uploadImages(long id, MultipartFile[] images) {
        Optional<Product> productOpt = productRepository.findById(id);
        if(productOpt.isPresent()){
            Product product = productOpt.get();

            String folderRelativeLocation = "/images/products/";
            String folderLocation = System.getProperty("user.dir") + folderRelativeLocation;
            File imageFolder = new File(folderLocation);
            if (!imageFolder.exists())
                imageFolder.mkdirs();

            List<ProductImages> productImages = new ArrayList<>();
            boolean success = saveImages(productImages, images, folderLocation, folderRelativeLocation);

            if(success){
                productImages.forEach(im -> product.getProductImages().add(im));
                productRepository.save(product);
                return new ResponseMessage("Updated Successfully.", HttpStatus.OK);
            }
        }
        return new ResponseMessage("Bad request", HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllByUserId(long id) {
        return productRepository.findAllByUser_Id(id);
    }

    @Override
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public ResponseMessage deleteById(long id) {
        return null;
    }

    @Override
    public ResponseMessage deleteImagesByImageId(long id) {
        productImageRepository.deleteById(id);
        return new ResponseMessage("Deleted.", HttpStatus.OK);
    }

    private boolean saveImages(List<ProductImages> productImages, MultipartFile[] mFiles,
                               String folderLocation, String folderRelativeLocation){
        boolean success = true;
        for(MultipartFile image: mFiles){
            try{
                String extension = Objects
                        .requireNonNull(image.getOriginalFilename())
                        .substring(Objects.requireNonNull(image.getOriginalFilename()).lastIndexOf('.')+1);

                if(extension.equalsIgnoreCase("jpeg") ||extension.equalsIgnoreCase("png") ||
                        extension.equalsIgnoreCase("jpg")) {
                    String filename = System.currentTimeMillis() + "_"
                            + image.getOriginalFilename().substring(0, image.getOriginalFilename().lastIndexOf("."))
                            .replaceAll(" ", "_");

                    String imageLoc = folderLocation + filename + "." + extension;
                    String relativeImageLoc = folderRelativeLocation +
                            filename + "." + extension;

                    File imageFile = new File(imageLoc);
                    image.transferTo(imageFile);

                    productImages.add(
                            new ProductImages(
                                    filename,
                                    extension,
                                    imageLoc,
                                    relativeImageLoc
                            )
                    );
                }
                else{
                    success = false;
                    break;
                }
            }
            catch (IOException ex){
                throw new ProductException("Problem occured while saving product.",ex);
            }
        }
        return success;
    }
}
