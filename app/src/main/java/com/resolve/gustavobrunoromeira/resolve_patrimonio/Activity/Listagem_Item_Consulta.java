package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.API.ResolvePatrimonio;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterBemConsulta;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.BemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSharedPreferences;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.IFNULL;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RecyclerItemClickListener;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RetrofitConfig;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Listagem_Item_Consulta extends AppCompatActivity {

    // Variaveis de Modelos
    private Usuario usuario;
    private Bem bem = new Bem();

    // Variaveis de API e Conexao
    private Retrofit retrofit;
    private ResolvePatrimonio resolvePatrimonio;
    private ConfiguracaoSharedPreferences preferences;

    // Listas de Controle
    private List<Bem> bens = new ArrayList<>();
    private List<Bem> bensLocal = new ArrayList<>();
    private List<Bem> bensWEB = new ArrayList<>();

    // Variaveis de Adapter
    private AdapterBemConsulta adapter;

    // Variavel de Sistemas
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    // Variavel de Controle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_item_consulta);

        retrofit = RetrofitConfig.getRetrofit();
        resolvePatrimonio = retrofit.create(ResolvePatrimonio.class);

        preferences =  new ConfiguracaoSharedPreferences( getApplicationContext() );
        usuario = preferences.recupraDadosPessoais();

        bem = (Bem) getIntent().getSerializableExtra("Bem");

        recyclerView = findViewById(R.id.recycle_Listagem_Item_ConsultaID);
        toolbar      = findViewById(R.id.toolbar);
        retrofit     = RetrofitConfig.getRetrofit();

        toolbar.setTitle(R.string.Nav2);
        setSupportActionBar( toolbar );

        iniciaViews();

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener( getApplicationContext(),  recyclerView,  new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent (getApplicationContext(), CadastroBem.class).putExtra("Bem",bens.get(position)));
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }

        } ));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        iniciaViews();
    }

    /**Metodo Responsavel por inicia o Recycle Views
     */
    private void iniciaViews(){

        if (!bens.isEmpty()){
            bens.clear();
        }

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        consultaWeb();
        consultaLocal();

    }

    /**Metodo Responsavel Consulta o Bens WEB
     */
    public void consultaWeb(){

        final ProgressDialog progressDialog = new ProgressDialog(Listagem_Item_Consulta.this);
        progressDialog.setMax(100);
        progressDialog.setTitle( R.string.Alerta3 );
        progressDialog.setMessage( getString( R.string.Alerta4 ) );
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();

        resolvePatrimonio.recuperaBem( usuario.getClienteIDFK()
                , IFNULL.verifica( String.valueOf( bem.getSecretariaIDFK()),"" )
                , IFNULL.verifica( String.valueOf( bem.getCentroCustoIDFK()),"" )
                , IFNULL.verifica( String.valueOf( bem.getLocalizacaoIDFK()),"" )
                , IFNULL.verifica( String.valueOf( bem.getResponsavelIDFK()),"" )
                , IFNULL.verifica( String.valueOf( bem.getItemIDFK()),"" )
                , IFNULL.verifica( String.valueOf( bem.getFabricanteIDFK()),"" )
                , IFNULL.verifica( String.valueOf( bem.getEstadoConservacaoIDFK()),"" )
                , IFNULL.verifica( String.valueOf( bem.getTipoTomboIDFK()),"" )
                , bem.getPlaqueta() )
                .enqueue(new Callback<List<Bem>>() {
                    @Override
                    public void onResponse(Call<List<Bem>> call, Response<List<Bem>> response) {

                        if (response.isSuccessful()) {

                            bensWEB = response.body();

                            if (bensWEB != null ) {

                                for (int i = 0; i < bensWEB.size(); i++) {
                                    bensWEB.get(i).setExportado(1);
                                }
                                bens.addAll(bensWEB);
                            }
                            progressDialog.dismiss();

                            adapter = new AdapterBemConsulta( bens,getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Bem>> call, Throwable t) {
                        progressDialog.dismiss();
                    }

                });
    }

    /**Metodo Responsavel Consulta o Bens Localmente
     */
    public void consultaLocal(){

        BemDAO bemDAO = new BemDAO(getApplicationContext());

        if (bensLocal.isEmpty()){
            bensLocal = bemDAO.Lista( bem );
        }else  {
            bensLocal.clear();
            bensLocal = bemDAO.Lista( bem );
        }

        bens.addAll(bensLocal);

        adapter = new AdapterBemConsulta( bens,getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}
