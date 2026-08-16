package com.mjgomes.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class S3Service {

    // Logger para registrar eventos e erros
    private Logger LOG = LoggerFactory.getLogger(S3Service.class);
    
    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucketName;

    /* @param multipartFile - Arquivo a ser enviado
    @return URI - URL do arquivo enviado
    */
    public URI uploadFile(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            InputStream inputStream = multipartFile.getInputStream();
            String contentType = multipartFile.getContentType();

            return upLoadFile(inputStream, fileName, contentType);
        } catch (IOException e) {
            throw new RuntimeException("Erro de IO: " + e.getMessage());
        }
    }

    /* @param inputStream - Fluxo de entrada do arquivo
    @param fileName - Nome do arquivo
    @param contentType - Tipo do arquivo
    @return URI - URL do arquivo enviado
    */
    public URI upLoadFile(InputStream inputStream, String fileName, String contentType){
        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(contentType);
            LOG.info("Iniciando Upload");
            s3Client.putObject(bucketName, fileName, inputStream, meta);
            LOG.info("Upload finalizado");
            return s3Client.getUrl(bucketName, fileName).toURI();
        } catch (AmazonServiceException e) {
            throw new RuntimeException("Erro ao fazer upload para o S3: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Erro ao converter URL para URI: " + e.getMessage());
        }
    }
}
