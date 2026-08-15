package com.mjgomes.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class S3Service {

    private Logger LOG = LoggerFactory.getLogger(S3Service.class);
    
    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucketName;

    public void uploadFile(String localFilePath) {
        try {
            // converter o MultipartFile para File
            File file = new File(localFilePath);
            LOG.info("Iniciando Upload");
            s3Client.putObject(new PutObjectRequest(bucketName, "teste.jpg", file));
            LOG.info("Upload finalizado");
        } catch (AmazonServiceException e) {
            LOG.info("AmazonServiceException: " + e.getErrorMessage());
            LOG.info("Status Code: " + e.getErrorCode());
        } catch (AmazonClientException e) {
            LOG.info("AmazonClientException: " + e.getMessage());
        }
    }
}
