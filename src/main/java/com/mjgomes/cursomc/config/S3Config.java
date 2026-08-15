package com.mjgomes.cursomc.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

// Configuração de conexão com Amazon S3 para armazenamento de imagens
@Configuration
public class S3Config {

    @Value("${aws.access_key_id}")
    private String awsAccessKeyId;

    @Value("${aws.secret_access_key}")
    private String awsSecretAccessKey;

    @Value("${s3.region}")
    private String awsRegion;

    // Criação do cliente S3 com as credenciais e região configuradas
    @Bean
    public AmazonS3 S3Client() {
        // Criação das credenciais
        BasicAWSCredentials awsCred = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);

        // Criação do cliente S3 com as credenciais e região configuradas
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCred))
                .withRegion(Regions.fromName(awsRegion))
                .build();

        return s3Client;
    }
}
