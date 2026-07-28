package com.mjgomes.cursomc.resources.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// Helpers para decodificar query params de busca (nome de produto, lista de ids de categoria).
public class URL {

    // Decodifica um parâmetro URL-encoded (ex: espaços/acentos); qualquer falha vira string vazia
    // para não quebrar a busca por causa de um parâmetro malformado.
    public static String decodeParam(String s) {
        try
        {
            return URLDecoder.decode(s, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            return "";
        }
            }

    // Converte "1,2,3" em [1, 2, 3]; usado para a lista de ids de categoria vinda da URL.
    public static List<Integer> decodeIntList(String s) {
        String[] split = s.split(",");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            list.add(Integer.parseInt(split[i]));
        }
        return list;
    }
}
