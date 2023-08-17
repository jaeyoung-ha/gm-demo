package com.aws.gmdemo.repository;

import com.aws.gmdemo.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    ProductEntity findByProductId(String productId);

    ProductEntity save(ProductEntity productEntity);

    void delete(ProductEntity reservationEntity);

}
