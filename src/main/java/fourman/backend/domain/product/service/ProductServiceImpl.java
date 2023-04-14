package fourman.backend.domain.product.service;

import fourman.backend.domain.product.controller.requestForm.EditProductRequestForm;
import fourman.backend.domain.product.controller.responseForm.AllProductResponseForm;
import fourman.backend.domain.product.controller.responseForm.ImageResourceResponseForm;
import fourman.backend.domain.product.controller.responseForm.ProductListResponseForm;
import fourman.backend.domain.product.controller.requestForm.ProductRequestForm;
import fourman.backend.domain.product.entity.ImageResource;
import fourman.backend.domain.product.entity.Product;
import fourman.backend.domain.product.repository.ImageResourceRepository;
import fourman.backend.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    final private ProductRepository productRepository;
    final private ImageResourceRepository imageResourceRepository;

    @Transactional
    @Override
    public void register(List<MultipartFile> imageFileList, ProductRequestForm productRequestForm) {

        List<ImageResource> imageResourceList = new ArrayList<>();

        final String fixedStringPath = "../FourMan-Front/src/assets/product/uploadImgs/";

        Product product = new Product();

        product.setProductName(productRequestForm.getProductName());
        product.setPrice(productRequestForm.getPrice());
        product.setDrinkType(productRequestForm.getDrinkType());

        try{
            for(MultipartFile multipartFile: imageFileList) {
                log.info("requestFileUploadWithText() - filename: " + multipartFile.getOriginalFilename());

                String fullPath = fixedStringPath + multipartFile.getOriginalFilename();

                FileOutputStream writer = new FileOutputStream(fullPath);

                writer.write(multipartFile.getBytes());
                writer.close();

                ImageResource imageResource = new ImageResource(multipartFile.getOriginalFilename());
                imageResourceList.add(imageResource);
                product.setImageResource(imageResource);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        productRepository.save(product);

        imageResourceRepository.saveAll(imageResourceList);

    }

    @Override
    public List<ProductListResponseForm> list() {
        List<Product> productList = productRepository.findAll();
        List<ProductListResponseForm> productResponseList = new ArrayList<>();

        for(Product product: productList) {
            productResponseList.add(new ProductListResponseForm(
                    product.getProductId(), product.getProductName(),
                    product.getPrice(),
                    product.getDrinkType()
            ));
        }

        return productResponseList;
    }

    @Override
    public List<ImageResourceResponseForm> loadProductImage() {
        List<ImageResource> imageResourceList = imageResourceRepository.findAll();
        List<ImageResourceResponseForm> imageResourceResponseFormList = new ArrayList<>();

        for(ImageResource imageResource: imageResourceList) {
            System.out.println("imageResource Path: " + imageResource.getImageResourcePath());

            imageResourceResponseFormList.add(new ImageResourceResponseForm(
                    imageResource.getImageResourcePath()
            ));
        }

        return imageResourceResponseFormList;
    }

    @Override
    public List<AllProductResponseForm> all() {
        List<Product> productList = productRepository.findAll();
        List<AllProductResponseForm> allProductList = new ArrayList<>();

        for (Product product: productList) {
            List<ImageResource> imageResourceList = imageResourceRepository.findImagePathListByProductId(product.getProductId());

            allProductList.add(new AllProductResponseForm(
                    product.getProductId(), product.getProductName(), product.getDrinkType(), product.getPrice(),
                    1, product.getPrice(), imageResourceList));
        }

        return allProductList;
    }

    @Transactional
    @Override
    public Product editProductWithImage(List<MultipartFile> editImageFileList, EditProductRequestForm editProductRequestForm) {

        Long productId = editProductRequestForm.getProductId();
        log.info("productId: " + productId);

        Optional<Product> maybeProduct = productRepository.findProductById(productId);
        List<ImageResource> imageResourceList = imageResourceRepository.findImagePathListByProductId(productId);

        ImageResource imageResource = imageResourceList.get(0);

        if(maybeProduct.isEmpty()) {
            System.out.println("Product 정보를 찾지 못했습니다: " + productId);
            return null;
        }

        final String fixedStringPath = "../FourMan-Front/src/assets/product/uploadImgs/";

        Product product = maybeProduct.get();

        product.setProductName(editProductRequestForm.getProductName());
        product.setPrice(editProductRequestForm.getPrice());
        product.setDrinkType(editProductRequestForm.getDrinkType());

        try {
            for(MultipartFile multipartFile: editImageFileList) {
                String fullPath = fixedStringPath + multipartFile.getOriginalFilename();

                FileOutputStream writer = new FileOutputStream(fullPath);

                writer.write(multipartFile.getBytes());
                writer.close();

                imageResource.setImageResourcePath(multipartFile.getOriginalFilename());
                imageResourceList.set(0, imageResource);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        productRepository.save(product);

        imageResourceRepository.saveAll(imageResourceList);

        return product;

    }

    @Transactional
    @Override
    public Product editProductWithoutImage(EditProductRequestForm editProductRequestForm) {

        Long productId = editProductRequestForm.getProductId();
        log.info("productId: " + productId);

        Optional<Product> maybeProduct = productRepository.findProductById(productId);

        if(maybeProduct.isEmpty()) {
            System.out.println("Product 정보를 찾지 못했습니다: " + productId);
            return null;
        }

        Product product = maybeProduct.get();

        product.setProductName(editProductRequestForm.getProductName());
        product.setPrice(editProductRequestForm.getPrice());
        product.setDrinkType(editProductRequestForm.getDrinkType());

        productRepository.save(product);

        return product;
    }

    @Override
    public void remove(Long productId) {
        productRepository.deleteById(productId);
    }

}
