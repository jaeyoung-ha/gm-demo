package com.aws.gmdemo;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.aws.gmdemo.utils.MultipartUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class AmazonS3Helper {

    @Value(value = "${s3.bucket-path}")
    private String bucket;

//    @Value(value = "${cloud.aws.credentials.access-key}")
//    private String accessKey;
//
//    @Value(value = "${cloud.aws.credentials.secret-key}")
//    private String secretKey;

    public String store(MultipartFile multipartFile) {
/*
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                accessKey, secretKey);
*/

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();


        String fileName = MultipartUtil.createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType((multipartFile.getContentType()));

        log.info("store() fileName : " + fileName);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Client.putObject(bucket, fileName, inputStream, objectMetadata);
        } catch (IOException e) {
            log.error("store() - occurred IllegalArgumentException");
            fileName = "";
        }

        return fileName;
    }

    public byte[] getFile(String filename) throws IOException {
/*
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                accessKey, secretKey);
*/

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        byte[] result = new byte[0];

        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, filename);
            S3Object s3Object = s3Client.getObject(getObjectRequest);
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

            result = IOUtils.toByteArray(objectInputStream);
        } catch (AmazonS3Exception e) {
            log.error("getFile() - occurred AmazonS3Exception");
            return new byte[0];
        }

        return result;
    }
}
