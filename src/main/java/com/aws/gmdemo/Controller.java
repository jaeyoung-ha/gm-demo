package com.aws.gmdemo;

import com.aws.gmdemo.constants.StatusCodeConstants;
import com.aws.gmdemo.dto.CommonReturnDto;
import com.aws.gmdemo.dto.ProductDto;
import com.aws.gmdemo.vo.RequestProduct;
import com.aws.gmdemo.vo.ResponseGetInfo;
import com.aws.gmdemo.vo.ResponseProduct;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping
public class Controller {
    private Environment env;

    ProductService productService;
    AmazonS3Helper amazonS3Helper;

    @Autowired
    public Controller(Environment env, AmazonS3Helper amazonS3Helper, ProductService productService) {
        this.env = env;
        this.amazonS3Helper = amazonS3Helper;
        this.productService = productService;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("Gentle Monster Demo - Port: %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/upload/file")
    public String uploadFile(@RequestPart("file") MultipartFile multipartFile) {

        // Store Image file to S3
        String fileName = amazonS3Helper.store(multipartFile);

        return "Upload File - Success : fileName" + fileName;
    }

    @GetMapping("/getPhoto/{filename}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("filename") String filename) {
        log.info("/getPhoto - filename : " + filename);

        byte[] photoImg = new byte[0];

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        if (TextUtils.isEmpty(filename) || "undefined".equals(filename)) {
            return new ResponseEntity<>(photoImg, headers, HttpStatus.BAD_REQUEST);
        }

        try {
            photoImg = amazonS3Helper.getFile(filename);
        } catch (IOException e) {
            log.error("/getPhoto occurred IOException");
        }

        return new ResponseEntity<>(photoImg, headers, HttpStatus.OK);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<CommonReturnDto<ResponseProduct>> getProduct(@RequestBody RequestProduct productDetails) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductDto productDto = mapper.map(productDetails, ProductDto.class);
        productService.createReserve(productDto);

        ResponseProduct responseProduct = mapper.map(productDto, ResponseProduct.class);

        return new ResponseEntity<>(
                CommonReturnDto.<ResponseProduct>builder()
                        .statusCode(TextUtils.isEmpty(productDto.getErrCode()) ? StatusCodeConstants.okCodeRequestSuccess : productDto.getErrCode())
                        .statusMsg(TextUtils.isEmpty(productDto.getErrMsg()) ? StatusCodeConstants.okDescRequestSuccess : productDto.getErrMsg())
                        .data(responseProduct)
                        .build(),
                HttpStatus.OK);
    }

    @GetMapping("/getProduct/{productId}")
    public ResponseEntity<CommonReturnDto<ResponseGetInfo>> getProduct(@PathVariable("productId") String productId) {

        ProductDto bookingDto = productService.getProductByProductId(productId);
        ResponseGetInfo responseGetInfo = new ModelMapper().map(bookingDto, ResponseGetInfo.class);

        return new ResponseEntity<>(
                CommonReturnDto.<ResponseGetInfo>builder()
                        .statusCode(TextUtils.isEmpty(bookingDto.getErrCode()) ? StatusCodeConstants.okCodeRequestSuccess : bookingDto.getErrCode())
                        .statusMsg(TextUtils.isEmpty(bookingDto.getErrMsg()) ? StatusCodeConstants.okDescRequestSuccess : bookingDto.getErrMsg())
                        .data(responseGetInfo)
                        .build(),
                HttpStatus.OK);
    }



}
