package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.API.ResolvePatrimonio;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterBemExportados;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSharedPreferences;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RecyclerItemClickListener;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RetrofitConfig;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Listagem_Item_Exportados extends AppCompatActivity {

    // Variaveis de Modelos
    private Usuario usuario;

    // Variaveis de API e Conexao
    private Retrofit retrofit;
    ResolvePatrimonio resolvePatrimonio;

    // Listas de Controle
    private List<Bem> bens = new ArrayList<>();

    // Variaveis de Adapter
    private AdapterBemExportados adapter;

    // Variavel de Sistemas
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;

    // Variavel de Controle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_item_exportados);

        retrofit = RetrofitConfig.getRetrofit();
        resolvePatrimonio = retrofit.create(ResolvePatrimonio.class);

        usuario = new ConfiguracaoSharedPreferences( getApplicationContext() ).recupraDadosPessoais();

        recyclerView = findViewById(R.id.recycle_Listagem_Item_ExportadosID);
        toolbar      = findViewById(R.id.toolbar);
        searchView   = findViewById(R.id.search_view);

        toolbar.setTitle(R.string.Titulo2);
        setSupportActionBar(toolbar);

        consultaWeb();
        iniciaViews();

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener( getApplicationContext(),  recyclerView,  new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                bens.get(position).setExportado(1);
                startActivity(new Intent(getApplicationContext(), CadastroBem.class).putExtra("Bem",bens.get(position)));

            }

            @Override
            public void onLongItemClick(View view, int position) {
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }

        } ));

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                iniciaViews();
                pesquisaPlaqueta(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                onRestart();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        consultaWeb();

        adapter = new AdapterBemExportados(bens,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);

        searchView.setMenuItem(menuItem);

        return true;

    }

    /**Metodo que recupera as plaquetas para serem carregadas no Recycle View*/
    public void consultaWeb(){

        final ProgressDialog progressDialog = new ProgressDialog(Listagem_Item_Exportados.this);
        progressDialog.setMax(100);
        progressDialog.setTitle( getString( R.string.Alerta3 ) );
        progressDialog.setMessage( getString( R.string.Alerta4 ) );
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();

        resolvePatrimonio.recuperaBem( usuario.getClienteIDFK()
                                    , ""
                                    , ""
                                    , ""
                                    , ""
                                    , ""
                                    , ""
                                    , ""
                                    , ""
                                    , "" )
                .enqueue(new Callback<List<Bem>>() {
                    @Override
                    public void onResponse(Call<List<Bem>> call, Response<List<Bem>> response) {

                        if (response.isSuccessful()) {

                            bens = response.body();
                            progressDialog.dismiss();

                            adapter = new AdapterBemExportados(bens,getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Bem>> call, Throwable t) {
                    }
                });

    }

    /**Metodo que Pesquisa no Banco de dados a Plaqueta digitada
     * @param plaqueta Plaqueta a ser Pesquisada*/
    public void pesquisaPlaqueta(String plaqueta){

        bens.clear();

        resolvePatrimonio.recuperaBem( usuario.getClienteIDFK()
                                    , ""
                                    , ""
                                    , ""
                                    , ""
                                    , ""
                                    , ""
                                    , ""
                                    , ""
                                    ,  plaqueta )
                        .enqueue(new Callback<List<Bem>>() {
                        @Override
                        public void onResponse(Call<List<Bem>> call, Response<List<Bem>> response) {

                            if (response.isSuccessful()) {

                                bens = response.body();

                                adapter = new AdapterBemExportados(bens,getApplicationContext());
                                recyclerView.setAdapter(adapter);

                            }
                        }

                        @Override
                        public void onFailure(Call<List<Bem>> call, Throwable t) {
                        }
                    });
    }

    /**Metodo Responsavel por inicia o Recycle Views*/
    private void iniciaViews(){

        recyclerView = findViewById(R.id.recycle_Listagem_Item_ExportadosID);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        consultaWeb();

    }

}