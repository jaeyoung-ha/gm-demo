package com.aws.gmdemo.utils;

import com.aws.gmdemo.dto.ProductDto;
import com.aws.gmdemo.entity.ProductEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Random;

public class DtoUtil {

    public static ProductEntity convertToReserveEntity(ProductDto productDto) {
        ModelMapper mapper = new CustomModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(productDto, ProductEntity.class);
    }
    public static ProductDto convertToReserveDto(ProductEntity productEntity) {
        ModelMapper mapper = new CustomModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(productEntity, ProductDto.class);
    }

    private static String[] items = {"5678*", "2580*", "1470*", "3690*"};
    private static Random rand = new Random();

    public static String makePassword() {
        String password = items[rand.nextInt(items.length)];
        return password;
    }
}
