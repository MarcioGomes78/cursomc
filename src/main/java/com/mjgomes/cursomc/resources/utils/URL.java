package com.mjgomes.cursomc.resources.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class URL {

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

    public static List<Integer> decodeIntList(String s) {
        // Split the string by commas, convert each part to an Integer, and collect into a List
        //List.of(s.split(",")).stream().map(x -> Integer.parseInt(x)).toList();
        String[] split = s.split(",");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            list.add(Integer.parseInt(split[i]));
        }
        return list;
    }
}
