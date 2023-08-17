package com.aws.gmdemo;

import com.aws.gmdemo.dto.ProductDto;
import com.aws.gmdemo.entity.ProductEntity;
import com.aws.gmdemo.repository.ProductRepository;
import com.aws.gmdemo.utils.DtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDto getProductByProductId(String productId) {
        ProductEntity productEntity = productRepository.findByProductId(productId);
        ProductDto productDto = DtoUtil.convertToReserveDto(productEntity);

        return productDto;
    }
}
