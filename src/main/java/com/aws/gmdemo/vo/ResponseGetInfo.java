package com.aws.gmdemo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseGetInfo {
    private String productId;
    private String name;
    private Integer price;
}
