package com.bookmystay.app.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bookmystay.app.exception.OurException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class AwsS3Service {
    private static final String BUCKET_NAME = "book-my-stay-hotel-images";
    private static final Logger logger = LoggerFactory.getLogger(AwsS3Service.class);


    @Value("${aws.s3.access.key}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secret.key}")
    private String awsS3SecretKey;

    public String saveImageToS3(MultipartFile photo) {

        try {
            String s3FileName = photo.getOriginalFilename();
            AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(credentialsProvider)
                    .withRegion(Regions.EU_CENTRAL_1)
                    .build();


            InputStream inputStream = photo.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, s3FileName, inputStream, metadata);
            s3Client.putObject(putObjectRequest);
            return "https://" + BUCKET_NAME + ".s3.amazonaws.com/" + s3FileName;

        } catch (Exception e) {
        String errorMessage = "Unable to upload image to S3 bucket";
        logger.error("{}: {}", errorMessage, e.getMessage(), e);
        throw new OurException(errorMessage);
    }


}
}
