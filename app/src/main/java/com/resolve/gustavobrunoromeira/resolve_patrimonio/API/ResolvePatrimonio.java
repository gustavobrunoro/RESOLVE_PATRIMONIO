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
import rx.Observable;

public interface ResolvePatrimonio {

    @GET("Bem")
    Call<List<Bem>> recuperaBem(@Query("ClienteIDFK") int ClienteIDFK,
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
    Call<Integer> totalBem(@Query("ClienteIDFK") int ClienteIDFK);

    @GET("Secretaria")
    Call<List<Secretaria>> recuperaSecretaria(@Query("ClienteIDFK") int ClienteIDFK,
                                              @Query("SecretariaID") String SecretariaID);

    @GET("CentroCusto")
    Call<List<CentroCusto>> recuperaCentroCusto(@Query("ClienteIDFK") int ClienteIDFK,
                                                @Query("SecretariaIDFK") String SecretariaIDFK,
                                                @Query("CentroCustoID") String CentroCustoID);

    @GET("CentroCustoLocalResponsavel")
    Call<List<CentroCustoLocalResponsavel>> recuperaCentroCustoLocalResponsavel(@Query("ClienteIDFK") int ClienteIDFK,
                                                                                @Query("CentroCustoIDFK") String CentroCustoIDFK,
                                                                                @Query("LocalizacaoIDFK") String LocalizacaoIDFK);


    @GET("Localizacao")
    Call<List<Localizacao>> recuperaLocalizacao(@Query("ClienteIDFK") int ClienteIDFK,
                                                @Query("LocalizacaoID") String LocalizacaoID);

    @GET("Responsavel")
    Call<List<Responsavel>> recuperaResponsavel(@Query("ClienteIDFK") int ClienteIDFK,
                                                @Query("ResponsavelID") String ResponsavelID);

    @GET("Item")
    Call<List<Item>> recuperaItem(@Query("ClienteIDFK") int ClienteIDFK,
                                  @Query("ItemID") String ItemID);

    @GET("Fabricante")
    Call<List<Fabricante>> recuperaFabricante(@Query("ClienteIDFK") int ClienteIDFK,
                                              @Query("FabricanteID") String FabricanteID);

    @GET("TipoTombo")
    Call<List<TipoTombo>> recuperaTipoTombo();

    @GET("EstadoConservacao")
    Call<List<EstadoConservacao>> recuperaEstadoConservacao();

    @GET("Permissao")
    Call<List<PermissaoExportar>> recuperaPermissao(@Query("ClienteIDFK") int ClienteIDFK);

    @POST("Bem")
    Call<Bem> enviarBens (@Body Bem bem);

}


