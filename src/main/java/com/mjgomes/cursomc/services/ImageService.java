package com.mjgomes.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mjgomes.cursomc.services.exceptions.FileException;

@Service
public class ImageService {

    public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {

        // Pega a extensão do arquivo
        String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());

        // Se a extensão não for jpg, png ou jpeg, lança um erro
        if (!"png".equalsIgnoreCase(ext) && !"jpeg".equalsIgnoreCase(ext) && !"jpg".equalsIgnoreCase(ext)) {
            throw new FileException("Apenas arquivos PNG e JPEG são permitidos");
        }

        try {
            BufferedImage bufImg = ImageIO.read(uploadedFile.getInputStream());
            // SE FOR PNG, CONVERTE PARA JPG
            if ("png".equalsIgnoreCase(ext)) {
                bufImg = pngToJpg(bufImg);
            }

            return bufImg;
        } catch (IOException e) {
            throw new FileException("Erro ao ler arquivo");
        }
    }

    // Método que converte uma imagem PNG para JPG
    private BufferedImage pngToJpg(BufferedImage bufImg) {
        // Cria uma nova imagem JPG
        BufferedImage jpgImage = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        // Desenha a imagem PNG na imagem JPG
        jpgImage.createGraphics().drawImage(bufImg, 0, 0, Color.WHITE, null);
        // Retorna a imagem JPG
        return jpgImage;
    }

    //Recebe uma Imagem e a extensão e retorna um InputStream
    public InputStream getInputStream(BufferedImage bufImg, String extension) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufImg, extension, os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            throw new FileException("Erro ao ler arquivo");
        }
    }
}