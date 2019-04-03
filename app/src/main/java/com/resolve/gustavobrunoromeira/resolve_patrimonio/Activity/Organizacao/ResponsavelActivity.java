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

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterResponsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.ResponsavelDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RecyclerItemClickListener;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Responsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class ResponsavelActivity extends AppCompatActivity {

    private List<Responsavel> responsaveis = new ArrayList<>();
    private Responsavel responsavel = new Responsavel();

    Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private AdapterResponsavel adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsavel);

        recyclerView = findViewById(R.id.recycle_ResponsavelID);
        toolbar      = findViewById(R.id.toolbar);
        searchView   = findViewById(R.id.search_view);

        toolbar.setTitle(R.string.Titulo7);
        setSupportActionBar(toolbar);

        recuperaResponsavel();

        //Configuração do Adpter
        adapter = new AdapterResponsavel(responsaveis, this);

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

                pesquisaResponsavel(newText);
                return true;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                recuperaResponsavel();
                onRestart();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        recuperaResponsavel();

        adapter = new AdapterResponsavel(responsaveis,this);
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

    /**Metodo Reponsavel por Recupera Responsavel */
    private void recuperaResponsavel() {

        Bem bem = new Bem();

        ResponsavelDAO responsavelDAO = new ResponsavelDAO(getApplicationContext());

        if (responsaveis.isEmpty()) {
            responsaveis = responsavelDAO.Lista(bem);
        } else {
            responsaveis.clear();
            responsaveis = responsavelDAO.Lista(bem);
            adapter.notifyDataSetChanged();
        }
    }

    /**Metodo Reponsavel por Pesquisa Responsavel */
    public void pesquisaResponsavel(String reponsavel){

        Bem bem = new Bem();

        ResponsavelDAO responsavelDAO = new ResponsavelDAO(getApplicationContext());

        if (responsaveis.isEmpty()){
            responsaveis = responsavelDAO.Lista(bem);
        }else  {
            responsaveis.clear();
            responsaveis = responsavelDAO.Pesquisa(reponsavel);
            adapter = new AdapterResponsavel(responsaveis,this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

}
