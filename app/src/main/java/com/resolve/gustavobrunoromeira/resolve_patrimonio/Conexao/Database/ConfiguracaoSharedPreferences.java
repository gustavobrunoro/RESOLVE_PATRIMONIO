package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;

public class ConfiguracaoSharedPreferences {

    public Context context;
    public static final String DADOSPESSOAIS = "DADOSPESSOAIS";
    public static final String BENS = "BENS";
    public static final String TOTAL = "TOTAL";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor ;

    /**Metodos Responsavel Retorna Configurar SharedPreferences*/
    public ConfiguracaoSharedPreferences(Context context) {

        this.context = context;
        sharedPreferences = context.getSharedPreferences(BENS,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    /**Metodos Responsavel Atualizar Total no SharedPreferences*/
    public void atualizaTotal(String total){

        editor.putString(TOTAL,total);
        editor.commit();

    }

    /**Metodos Responsavel Recupera Total no SharedPreferences
     @return  Total de Bem*/
    public String recuperaTotal(){

        return sharedPreferences.getString(TOTAL,"0");

    }

    /**Metodos Responsavel Atualizar Dados Pessoais no SharedPreferences*/
    public void atualizaDadosPessoais(Usuario usuario){

        Gson gson = new Gson();
        String json = gson.toJson(usuario);
        editor.putString(DADOSPESSOAIS, json);
        editor.commit();

    }

    /**Metodos Responsavel Recupera Dados Pessoais no SharedPreferences
     @return  Dados do Usuario*/
    public Usuario recupraDadosPessoais(){

        Usuario usuario = new Usuario();

        Gson gson = new Gson();
        String json = sharedPreferences.getString(DADOSPESSOAIS, "");
        usuario = gson.fromJson(json, Usuario.class);

        return usuario ;

    }

}
