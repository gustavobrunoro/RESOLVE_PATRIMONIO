package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterBem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.BemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSharedPreferences;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RecyclerItemClickListener;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Listagem_Item extends AppCompatActivity {


    // Variaveis de Modelos
    private Bem bem = new Bem();
    private Usuario usuario;

    // Variaveis de API e Conexao

    // Listas de Controle
    private List<Bem> bens = new ArrayList<>();

    // Variaveis de Adapter
    private AdapterBem adapter;


    // Variavel de Sistemas
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private ImageView imgCamera;
    private ImageView imgClearSearch;
    private File caminhoFoto;

    // Variavel de Controle
    private String caminhoFotoPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_item);

        usuario = new ConfiguracaoSharedPreferences( getApplicationContext() ).recupraDadosPessoais();
        caminhoFotoPrincipal = "/Resolve Patrimonio/" + usuario.getClienteIDFK() + "/Fotos/";

        recyclerView   = findViewById(R.id.recycle_Listagem_ItemID);
        toolbar        = findViewById(R.id.toolbar);
        searchView     = findViewById(R.id.search_view);
        imgCamera      = findViewById(R.id.imgCamera);
        imgClearSearch = findViewById(R.id.imgClearSearch);

        toolbar.setTitle(R.string.Titulo1);
        setSupportActionBar(toolbar);

        consultaLocal();
        swipe();

        //Configuração do Adpter
        adapter = new AdapterBem(usuario , bens ,this);

        //Configurando o Recycle View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

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

        searchView.setVoiceSearch(false);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                pesquisaPlaqueta(newText);
                return true;
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

                consultaLocal();
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

        consultaLocal();

        adapter = new AdapterBem(usuario , bens ,this);
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

        return  true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {

                BemDAO bemDAO = new BemDAO(getApplicationContext());

                if( ! bemDAO.Pesquisa( Integer.valueOf( result.getContents()).toString() ,0).isEmpty() ) {

                    bem = bemDAO.Pesquisa( Integer.valueOf( result.getContents()).toString() ,0).get(0);

                    startActivity(new Intent(getApplicationContext(), CadastroBem.class).putExtra("Bem", bem));

                }else{
                    Toast.makeText(getApplicationContext(), R.string.Mensagem5 ,Toast.LENGTH_LONG).show();
                }

            } else {
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**Metodo que recupera a Plaqueta no Banco de dados de Dados*/
    public void consultaLocal(){

        BemDAO bemDAO = new BemDAO(getApplicationContext());

        if (bens.isEmpty()){
            bens = bemDAO.Lista(0 );
        }else  {
            bens.clear();
            bens = bemDAO.Lista(0 );
            adapter.notifyDataSetChanged();
        }
    }

    /**Metodo que Pesquisa no Banco de dados a Plaqueta digitada
     * @param plaqueta Plaqueta a ser Pesquisada*/
    public void pesquisaPlaqueta(String plaqueta){

        BemDAO bemDAO = new BemDAO(getApplicationContext());

        if (bens.isEmpty()){
            bens = bemDAO.Lista(0 );
        }else  {
            bens.clear();
            bens = bemDAO.Pesquisa( plaqueta , 0);
            adapter = new AdapterBem(usuario , bens ,this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

    /**Metodo para Excluir a Plaqueta*/
    public void excluirPlaqueta(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final BemDAO bemDAO = new BemDAO(getApplicationContext());

        alertDialog.setTitle( R.string.Alerta2 );
        alertDialog.setIcon( R.drawable.ic_deleta_24dp );
        alertDialog.setMessage( R.string.Mensagem2 );
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Recebe a posição do item no Adapter
                int position = viewHolder.getAdapterPosition();

                //Recebe as informações do bem na posição
                bem = bens.get(position);

                //Remove o bem do SQLite
                bemDAO.Deletar(bem);

                // Remove o Bem da Listagem
                bens.remove(position) ;

                //Notifica o Adapter com o bem excluido
                adapter.notifyItemRemoved( position );

                caminhoFoto = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), caminhoFotoPrincipal + bem.getPlaqueta() + "_1" +".png");
                if (caminhoFoto.exists()){
                    caminhoFoto.delete();
                }

                caminhoFoto = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), caminhoFotoPrincipal + bem.getPlaqueta() + "_2" +".png");
                if (caminhoFoto.exists()){
                    caminhoFoto.delete();
                }
            }
        });

        alertDialog.setNegativeButton("Cancela", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    /**Metodo responsavel por Efetuar o Deslize para o lado para Excluir
     */
    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags= ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                 excluirPlaqueta(viewHolder);

            }

        };
        new ItemTouchHelper( itemTouch ).attachToRecyclerView(recyclerView);
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
