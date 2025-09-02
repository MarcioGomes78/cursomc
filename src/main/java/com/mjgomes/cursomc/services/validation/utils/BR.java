package com.mjgomes.cursomc.services.validation.utils;

public class BR {

    public static boolean isValidCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;
        int d1 = 0, d2 = 0;
        for (int i = 0; i < 9; i++) {
            int n = cpf.charAt(i) - '0';
            d1 += n * (10 - i);
            d2 += n * (11 - i);
        }
        d1 = d1 % 11 < 2 ? 0 : 11 - d1 % 11;
        d2 += d1 * 2;
        d2 = d2 % 11 < 2 ? 0 : 11 - d2 % 11;
        return d1 == cpf.charAt(9) - '0' && d2 == cpf.charAt(10) - '0';
    }

    public static boolean isValidCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) return false;
        int[] w1 = {5,4,3,2,9,8,7,6,5,4,3,2}, w2 = {6,5,4,3,2,9,8,7,6,5,4,3,2};
        int d1 = 0, d2 = 0;
        for (int i = 0; i < 12; i++) {
            int n = cnpj.charAt(i) - '0';
            d1 += n * w1[i];
            d2 += n * w2[i];
        }
        d1 = d1 % 11 < 2 ? 0 : 11 - d1 % 11;
        d2 += d1 * w2[12];
        d2 = d2 % 11 < 2 ? 0 : 11 - d2 % 11;
        return d1 == cnpj.charAt(12) - '0' && d2 == cnpj.charAt(13) - '0';
    }
}