package com.resolve.gustavobrunoromeira.resolve_patrimonio.API;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCusto;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCustoLocalResponsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.EstadoConservacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Fabricante;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Item;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Localizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.PermissaoExportar;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Responsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Secretaria;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.TipoTombo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ResolvePatrimonio {

    @GET("Bem")
    Call<List<Bem>> recuperaBem(@Query("ClienteIDFK") String ClienteIDFK,
                                @Query("SecretariaIDFK") String SecretariaIDFK,
                                @Query("CentroCustoIDFK") String CentroCustoIDFK,
                                @Query("LocalizacaoIDFK") String LocalizacaoIDFK,
                                @Query("ResponsavelIDFK") String ResponsavelIDFK,
                                @Query("ItemIDFK") String ItemIDFK,
                                @Query("FabricanteIDFK") String FabricanteIDFK,
                                @Query("EstadoConservacaoIDFK") String EstadoConservacaoIDFK,
                                @Query("TipoTomboIDFK") String TipoTomboIDFK,
                                @Query("Plaqueta") String Plaqueta);

    @GET("Bem/TotalBem")
    Call<String> totalBem(@Query("ClienteIDFK") String ClienteIDFK);

    @GET("Secretaria")
    Call<List<Secretaria>> recuperaSecretaria(@Query("ClienteIDFK") String ClienteIDFK,
                                              @Query("SecretariaID") String SecretariaID);

    @GET("CentroCusto")
    Call<List<CentroCusto>> recuperaCentroCusto(@Query("ClienteIDFK") String ClienteIDFK,
                                                @Query("SecretariaIDFK") String SecretariaIDFK,
                                                @Query("CentroCustoID") String CentroCustoID);

    @GET("CentroCustoLocalResponsavel")
    Call<List<CentroCustoLocalResponsavel>> recuperaCentroCustoLocalResponsavel(@Query("ClienteIDFK") String ClienteIDFK,
                                                                                @Query("CentroCustoIDFK") String CentroCustoIDFK,
                                                                                @Query("LocalizacaoIDFK") String LocalizacaoIDFK);


    @GET("Localizacao")
    Call<List<Localizacao>> recuperaLocalizacao(@Query("ClienteIDFK") String ClienteIDFK,
                                                @Query("LocalizacaoID") String LocalizacaoID);

    @GET("Responsavel")
    Call<List<Responsavel>> recuperaResponsavel(@Query("ClienteIDFK") String ClienteIDFK,
                                                @Query("ResponsavelID") String ResponsavelID);

    @GET("Item")
    Call<List<Item>> recuperaItem(@Query("ClienteIDFK") String ClienteIDFK,
                                  @Query("ItemID") String ItemID);

    @GET("Fabricante")
    Call<List<Fabricante>> recuperaFabricante(@Query("ClienteIDFK") String ClienteIDFK,
                                              @Query("FabricanteID") String FabricanteID);

    @GET("TipoTombo")
    Call<List<TipoTombo>> recuperaTipoTombo();

    @GET("EstadoConservacao")
    Call<List<EstadoConservacao>> recuperaEstadoConservacao();

    @GET("Permissao")
    Call<List<PermissaoExportar>> recuperaPermissao(@Query("ClienteIDFK") String ClienteIDFK);

    @POST("Bem")
    Call<Bem> enviarBens (@Body Bem bem);

    @POST("Bem")
    rx.Observable<Bem> enviarBensteste (@Body Bem bem);

    //@GET("Bem/Teste")
    //Observable<List<Bem>> enviarBensTeste (@Query("ClienteIDFK") String ClienteIDFK);

}


