package com.aws.gmdemo;

import com.aws.gmdemo.dto.ProductDto;
import com.aws.gmdemo.entity.ProductEntity;
import com.aws.gmdemo.repository.ProductRepository;
import com.aws.gmdemo.utils.DtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto createReserve(ProductDto productDto) {
        productDto.setProductId(UUID.randomUUID().toString());
        ProductEntity productEntity;

        log.info("booking is started");

        productEntity = DtoUtil.convertToReserveEntity(productDto);
        productRepository.save(productEntity);

        ProductDto returnDto = DtoUtil.convertToReserveDto(productEntity);

        log.info("booking is completed");

        return returnDto;
    }
    @Override
    public ProductDto getProductByProductId(String productId) {
        ProductEntity productEntity = productRepository.findByProductId(productId);
        ProductDto productDto = DtoUtil.convertToReserveDto(productEntity);

        return productDto;
    }
}
