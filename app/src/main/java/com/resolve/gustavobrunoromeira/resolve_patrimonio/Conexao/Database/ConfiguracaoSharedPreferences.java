package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Configuracoes;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;

public class ConfiguracaoSharedPreferences {

    public Context context;
    public static final String DADOSPESSOAIS = "DADOSPESSOAIS";
    public static final String CONFIGURACOES = "CONFIGURACOES";
    public static final String TOTAL = "TOTAL";
    public static final String RESOLVEPATRIMONIO = "RESOLVEPATRIMONIO";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor ;

    /**Metodos responsavel retorna configuração SharedPreferences*/
    public ConfiguracaoSharedPreferences(Context context) {

        this.context = context;
        sharedPreferences = context.getSharedPreferences(RESOLVEPATRIMONIO,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    /**Metodos responsavel atualizar total de bens no SharedPreferences*/
    public void atualizaTotal(int total){

        editor.putInt(TOTAL,total);
        editor.commit();

    }

    /**Metodos responsavel recupera total de bens no SharedPreferences
     @return  Total de Bem*/
    public Integer recuperaTotal(){

        return sharedPreferences.getInt(TOTAL,0);

    }

    /**Metodos responsavel atualizar dados pessoais no SharedPreferences*/
    public void atualizaDadosPessoais(Usuario usuario){

        Gson gson = new Gson();
        String json = gson.toJson(usuario);
        editor.putString(DADOSPESSOAIS, json);
        editor.commit();

    }

    /**Metodos responsavel por recupera os dados pessoais no SharedPreferences
     @return  Dados do Usuario*/
    public Usuario recupraDadosPessoais(){

        Usuario usuario = new Usuario();

        Gson gson = new Gson();
        String json = sharedPreferences.getString(DADOSPESSOAIS, "");
        usuario = gson.fromJson(json, Usuario.class);

        return usuario ;

    }

    /**Metodos responsavel atualizar dados pessoais no SharedPreferences*/
    public void atualizaConfiguracoes(Configuracoes configuracoes){

        Gson gson = new Gson();
        String json = gson.toJson(configuracoes);
        editor.putString(CONFIGURACOES, json);
        editor.commit();

    }

    /**Metodos responsavel por recupera as configurações no SharedPreferences
     @return  Dados do Usuario*/
    public Configuracoes recupraConfiguracoes(){

        Configuracoes configuracoes = new Configuracoes();

        Gson gson = new Gson();
        String json = sharedPreferences.getString(CONFIGURACOES, "");
        configuracoes = gson.fromJson(json, Configuracoes.class);

        return configuracoes ;

    }

    /**Metodos responsavel recupera codigo do cliente no SharedPreferences
     @return  Codigo do Cliente*/
    public int getClienteIDFK (){

        Usuario usuario = new Usuario();

        Gson gson = new Gson();
        String json = sharedPreferences.getString(DADOSPESSOAIS, "");
        usuario = gson.fromJson(json, Usuario.class);

        return usuario.getClienteIDFK() ;
    }

}
