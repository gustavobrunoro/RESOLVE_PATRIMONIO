package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.Organizacao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterLocalizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.LocalizacaoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RecyclerItemClickListener;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Localizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class LocalizacaoActivity extends AppCompatActivity {

    private List<Localizacao> localizacoes = new ArrayList<>();
    private Localizacao localizacao = new Localizacao();

    Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private AdapterLocalizacao adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);

        recyclerView = findViewById(R.id.recycle_LocalizacaoID);
        toolbar      = findViewById(R.id.toolbar);
        searchView   = findViewById(R.id.search_view);

        toolbar.setTitle(R.string.Titulo6);
        setSupportActionBar(toolbar);

        recuperaLocalizacoes();

        //Configuração do Adpter
        adapter = new AdapterLocalizacao(localizacoes, this);

        //Configurando o Recycle View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }

        }));

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                pesquisaLocalizacao(newText);
                return true;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                recuperaLocalizacoes();
                onRestart();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        recuperaLocalizacoes();

        adapter = new AdapterLocalizacao(localizacoes,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);

        searchView.setMenuItem(menuItem);

        return true;

    }

    /**Metodo Reponsavel por Recupera Localizações */
    private void recuperaLocalizacoes() {

        Bem bem = new Bem();

        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(getApplicationContext());

        if (localizacoes.isEmpty()) {
            localizacoes = localizacaoDAO.Lista(bem);
        } else {
            localizacoes.clear();
            localizacoes = localizacaoDAO.Lista(bem);
            adapter.notifyDataSetChanged();
        }
    }

    /**Metodo Reponsavel por Pesquisa Localização */
    public void pesquisaLocalizacao(String localizacao){

        Bem bem = new Bem();

        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(getApplicationContext());

        if (localizacoes.isEmpty()){
            localizacoes = localizacaoDAO.Lista(bem);
        }else  {
            localizacoes.clear();
            localizacoes = localizacaoDAO.Pesquisa(localizacao);
            adapter = new AdapterLocalizacao(localizacoes,this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

}
