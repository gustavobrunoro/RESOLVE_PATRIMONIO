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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.API.ResolvePatrimonio;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterBemExportados;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.BemDAO;
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
    private ResolvePatrimonio resolvePatrimonio;
    private ConfiguracaoSharedPreferences preferences;

    // Listas de Controle
    private List<Bem> bens = new ArrayList<>();

    // Variaveis de Adapter
    private AdapterBemExportados adapter;

    // Variavel de Sistemas
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private ImageView imgCamera;
    private ImageView imgClearSearch;

    // Variavel de Controle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_item_exportados);

        retrofit = RetrofitConfig.getRetrofit();
        resolvePatrimonio = retrofit.create(ResolvePatrimonio.class);

        preferences = new ConfiguracaoSharedPreferences( getApplicationContext() );
        usuario = preferences.recupraDadosPessoais();

        recyclerView = findViewById(R.id.recycle_Listagem_Item_ExportadosID);
        toolbar      = findViewById(R.id.toolbar);
        searchView   = findViewById(R.id.search_view);
        imgCamera    = findViewById(R.id.imgCamera);
        imgClearSearch = findViewById(R.id.imgClearSearch);

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

                imgCamera.setVisibility(View.VISIBLE);
                imgClearSearch.setVisibility(View.VISIBLE);
                searchView.setCloseIcon(getResources().getDrawable(R.drawable.ic_action_navigation_close_inverted));
            }

            @Override
            public void onSearchViewClosed() {
                onRestart();
                imgCamera.setVisibility(View.INVISIBLE);
                imgClearSearch.setVisibility(View.INVISIBLE);
            }
        });

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanneaPlaqueta();
            }
        });
        imgClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.closeSearch();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                pesquisaPlaqueta( String.valueOf( Integer.valueOf( result.getContents() )) );
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    /**Metodo Responsacel por Efetuar o Scaneamento da Plaqueta pela Câmera
     */
    public void scanneaPlaqueta(){

        IntentIntegrator leitor = new IntentIntegrator(this);
        leitor.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        leitor.setPrompt( getString( R.string.Mensagem12 ) );
        leitor.setCameraId(0);
        leitor.initiateScan();

    }
}