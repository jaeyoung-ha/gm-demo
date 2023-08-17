package com.aws.gmdemo;

import com.aws.gmdemo.dto.ProductDto;

public interface ProductService {
    ProductDto createReserve(ProductDto productDto);
    ProductDto getProductByProductId(String productId);
}
