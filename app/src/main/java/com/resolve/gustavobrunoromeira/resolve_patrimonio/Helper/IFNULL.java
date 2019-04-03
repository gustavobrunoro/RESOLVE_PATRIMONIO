package com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper;

public class IFNULL {

    /**Metodos Responsavel por Verificar se a Expressão e NULL
     @return  String */
    public static String verifica(String expressao,String retorno){

        if(expressao.toString() == null || expressao.toString() == "" || expressao.toString() == "null"){
            return retorno;
        }
        else{
            return expressao;
        }

    }

}
