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

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterSecretaria;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.SecretariaDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RecyclerItemClickListener;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Secretaria;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class SecretariaActivity extends AppCompatActivity {

    private List<Secretaria> secretarias = new ArrayList<>();
    private Secretaria secretaria = new Secretaria();

    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private AdapterSecretaria adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretaria);

        recyclerView = findViewById(R.id.recycle_SecretariaID);
        toolbar      = findViewById(R.id.toolbar);
        searchView   = findViewById(R.id.search_view);

        toolbar.setTitle(R.string.Titulo4);
        setSupportActionBar(toolbar);

        recuperaSecretarias();

        //Configuração do Adpter
        adapter = new AdapterSecretaria(secretarias,this);

        //Configurando o Recycle View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener( getApplicationContext(),  recyclerView,  new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                pesquisaSecretaria(newText);
                return true;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                recuperaSecretarias();
                onRestart();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        recuperaSecretarias();

        adapter = new AdapterSecretaria(secretarias,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);

        searchView.setMenuItem(menuItem);

        return  true;

    }

    /**Metodo Reponsavel por Recupera Secretarias */
    private void recuperaSecretarias(){

        SecretariaDAO secretariaDAO = new SecretariaDAO(getApplicationContext());

        if (secretarias.isEmpty()){
            secretarias = secretariaDAO.Lista();
        }else  {
            secretarias.clear();
            secretarias = secretariaDAO.Lista();
            adapter.notifyDataSetChanged();
        }
    }

    /**Metodo Reponsavel por Pesquisa Secretaria
     @param Secretaria Secretaria a se pesquisada */
    public void pesquisaSecretaria(String Secretaria){

        SecretariaDAO secretariaDAO = new SecretariaDAO(getApplicationContext());

        if (secretarias.isEmpty()){
            secretarias = secretariaDAO.Lista();
        }else  {
            secretarias.clear();
            secretarias = secretariaDAO.Pesquisa(Secretaria);
            adapter = new AdapterSecretaria(secretarias,this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

}
