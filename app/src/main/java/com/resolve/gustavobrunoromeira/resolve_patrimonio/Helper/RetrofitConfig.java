package com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.API.APIResolve;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {


    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(35, TimeUnit.SECONDS)
            .build();

    /**Metodos Responsavel por Retorna Paramentro de Conexão com a API
     @return  Paramento Retrofit*/
    public static Retrofit getRetrofit(){

        return new Retrofit.Builder()
                .baseUrl(APIResolve.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}

