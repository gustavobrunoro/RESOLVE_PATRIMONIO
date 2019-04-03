package com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper;

import android.util.Base64;

public class Base64Custom {

    /**Metodos Responsavel por Codificar String em Base64
     @return  String Codificada */
    public static String CodificarBase64(String base){

        return Base64.encodeToString(base.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)","");

    }

    /**Metodos Responsavel por Decodificar Base64
     @return  Base decodificada*/
    public static String DecodificarBase64(String base){

        return new String (Base64.decode(base,Base64.DEFAULT));

    }

}

