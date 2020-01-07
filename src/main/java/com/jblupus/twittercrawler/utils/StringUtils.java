package com.jblupus.twittercrawler.utils;

import java.text.Normalizer;

/**
 * Created by joao on 2/14/17.
 */
public class StringUtils {
    public static String unaccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }


    public static String unspecial(String src) {
        return Normalizer.normalize(src, Normalizer.Form.NFC).replaceAll("[ ](?=[ ])|[^-_,A-Za-z0-9 ]+", " ");
    }
    public static String toLowerCase(String src) {
        if(src!=null || src.length()>0) return src.toLowerCase();
        else return "";
    }

    public static void main(String[] args) {
        final String accents = "È,É,Ê,Ë,Û,Ù,Ï,Î,À,Â,Ô,è,é,ê,ë,û,ù,ï,î,à,â,ô,Ç,ç,Ã,ã,Õ,õ";
        String expected = "E,E,E,E,U,U,I,I,A,A,O,e,e,e,e,u,u,i,i,a,a,o,C,c,A,a,O,o";

        final String accents2 = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";
        String expected2 = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";

        final String accents3 = "Gisele Bündchen da Conceição e Silva foi batizada assim em homenagem à sua conterrânea de Horizontina, RS.";
        String expected3 = "Gisele Bundchen da Conceicao e Silva foi batizada assim em homenagem a sua conterranea de Horizontina, RS.";

        final String accents4 = "/Users/rponte/arquivos-portalfcm/Eletron/Atualização_Diária-1.23.40.exe";
        String expected4 = "/Users/rponte/arquivos-portalfcm/Eletron/Atualizacao_Diaria-1.23.40.exe";

        System.out.println(expected = unaccent(accents));
        System.out.println(expected2 = unaccent(accents2));
        System.out.println(expected3 = unaccent(accents3));
        System.out.println(expected4 = unaccent(accents4));


        System.out.println(unspecial(expected));
        System.out.println(unspecial(expected2));
        System.out.println(unspecial(expected3));
        System.out.println(unspecial(expected4));

    }

}
