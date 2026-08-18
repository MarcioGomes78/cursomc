package com.mjgomes.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
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
            //Cria um Stream de Bytes
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            //Escreve a Imagem no Stream
            ImageIO.write(bufImg, extension, os);
            //Retorna um InputStream com a Imagem
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            throw new FileException("Erro ao ler arquivo");
        }
    }

    //Cortar a imagem para que ela fique quadrada
    public BufferedImage cropSquare(BufferedImage sourceImg) {
        //Pega o menor lado da imagem
        int min = (sourceImg.getWidth() < sourceImg.getHeight() ? sourceImg.getWidth() : sourceImg.getHeight());
        //Calcula a posição x e y para centralizar o corte
        int x = (sourceImg.getWidth() - min) / 2;
        int y = (sourceImg.getHeight() - min) / 2;
        //Corta a imagem
        return Scalr.crop(sourceImg, x, y, min, min);
    }

    //Redimensionar a imagem para que ela tenha o tamanho especificado
    public BufferedImage resize(BufferedImage sourceImg, int size) {
        //Redimensiona a imagem
        return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
    }

}