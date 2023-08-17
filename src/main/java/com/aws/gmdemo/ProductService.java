package com.aws.gmdemo;

import com.aws.gmdemo.dto.ProductDto;

public interface ProductService {
    ProductDto getProductByProductId(String productId);
}
