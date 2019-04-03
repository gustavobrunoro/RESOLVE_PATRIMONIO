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

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterCentroCusto;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.CentroCustoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RecyclerItemClickListener;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCusto;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class CentroCustoActivity extends AppCompatActivity {

    private List<CentroCusto> centroCustos = new ArrayList<>();
    private CentroCusto centroCusto = new CentroCusto();

    Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private AdapterCentroCusto adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centro_custo);

        recyclerView = findViewById(R.id.recycle_CentroCustoID);
        toolbar      = findViewById(R.id.toolbar);
        searchView   = findViewById(R.id.search_view);

        toolbar.setTitle(R.string.Titulo5);
        setSupportActionBar(toolbar);

        recuperaCentroCustos();

        //Configuração do Adpter
        adapter = new AdapterCentroCusto(centroCustos, this);

        //Configurando o Recycle View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
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

                pesquisaCentroCusto(newText);
                return true;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                recuperaCentroCustos();
                onRestart();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        recuperaCentroCustos();

        adapter = new AdapterCentroCusto(centroCustos,this);
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

    /**Metodo Reponsavel por Recupera Centro de Custo */
    private void recuperaCentroCustos() {

        Bem bem = new Bem();

        CentroCustoDAO centroCustoDAO = new CentroCustoDAO(getApplicationContext());

        if (centroCustos.isEmpty()) {
            centroCustos = centroCustoDAO.Lista(bem);
        } else {
            centroCustos.clear();
            centroCustos = centroCustoDAO.Lista(bem);
            adapter.notifyDataSetChanged();
        }
    }

    /**Metodo Reponsavel por Pesquisa Centro de Custo */
    public void pesquisaCentroCusto(String cc){

        Bem bem = new Bem();

        CentroCustoDAO centroCustoDAO = new CentroCustoDAO(getApplicationContext());

        if (centroCustos.isEmpty()){
            centroCustos = centroCustoDAO.Lista(bem);
        }else  {
            centroCustos.clear();
            centroCustos = centroCustoDAO.Pesquisa(cc);
            adapter = new AdapterCentroCusto(centroCustos,this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

}
