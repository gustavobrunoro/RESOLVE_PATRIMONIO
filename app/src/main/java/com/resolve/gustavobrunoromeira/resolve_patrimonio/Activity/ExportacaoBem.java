package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.API.ResolvePatrimonio;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterExportacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.BemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSharedPreferences;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RetrofitConfig;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.PermissaoExportar;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExportacaoBem extends AppCompatActivity {

    // Variaveis de Modelos
    private Usuario usuario;

    // Variaveis de API e Conexao
    private Retrofit retrofit;
    private ResolvePatrimonio resolvePatrimonio;
    private ConfiguracaoSharedPreferences preferences;

    // Listas de Controle
    private List<Bem> bens = new ArrayList<>();
    private List<Bem> bensSelecionados = new ArrayList<>();
    private List<Bem> bensExportados = new ArrayList<>();

    // Variaveis de Adapter
    private AdapterExportacao adapter;

    // Variavel de Sistemas
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private AlertDialog alertDialog;
    private File caminhoFoto1;
    private File caminhoFoto2;
    private Bitmap Imagem1;
    private Bitmap Imagem2;
    private ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
    private ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
    private byte[] byteArray1;
    private byte[] byteArray2;

    // Variavel de Controle
    private int i = 1; // Variavel para Controle na Exportação das Plaquetas
    private String caminhoFotoPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportacao_bem);

        retrofit = RetrofitConfig.getRetrofit();
        resolvePatrimonio = retrofit.create(ResolvePatrimonio.class);

        preferences = new ConfiguracaoSharedPreferences( getApplicationContext() );
        usuario = preferences.recupraDadosPessoais();
        caminhoFotoPrincipal = "/Resolve Patrimonio/" + usuario.getClienteIDFK() + "/Fotos/";

        recyclerView = findViewById(R.id.recycleExportacaoId);
        toolbar      = findViewById(R.id.toolbar);
        searchView   = findViewById(R.id.search_view);
        retrofit     = RetrofitConfig.getRetrofit();

        toolbar.setTitle(R.string.Nav4);
        setSupportActionBar(toolbar);

        consultaLocal();

        //Configurando o Recycle View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Configurando o Adapter
        adapter = new AdapterExportacao(bens,this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        consultaLocal();

        adapter = new AdapterExportacao(bens,this);
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
        inflater.inflate(R.menu.menu_exportacao,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_Marca:
                adapter.marcaTudo();
                break;
            case R.id.action_Desmarca:
                adapter.desmarcaTudo();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("BensSelecionados", (Serializable) bensSelecionados);

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

    /**Metodo para Confirma a Exportação e Verifica se Tem Permissão para Exportar*/
    public void confirmaExportacao(View view ){

       final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.ic_nav_exportacao_24dp);
        alertDialog.setTitle(R.string.Nav4);
        alertDialog.setCancelable(false);

        if (bensSelecionados.isEmpty()) {
            bensSelecionados.addAll(adapter.ListaSelecionados());
        } else {
            bensSelecionados.clear();
            bensSelecionados.addAll(adapter.ListaSelecionados());
        }

        resolvePatrimonio.recuperaPermissao( usuario.getClienteIDFK() ).enqueue(new Callback<List<PermissaoExportar>>() {
            @Override
            public void onResponse(Call<List<PermissaoExportar>> call, Response<List<PermissaoExportar>> response) {

                PermissaoExportar permissao = response.body().get(0);

                if (response.isSuccessful()){

                    // Verificar se o Clientes pode Exporta as Plaquetas
                    if (permissao.getTipoPermissaoIDFK() == 1 && permissao.getStatus() == 0){

                        if (bensSelecionados.isEmpty()) {
                            bensSelecionados.addAll(adapter.ListaSelecionados());
                        } else {
                            bensSelecionados.clear();
                            bensSelecionados.addAll(adapter.ListaSelecionados());
                        }

                        if (bensSelecionados.size() > 0) {

                            alertDialog.setMessage("Confirma a Exportação de " + bensSelecionados.size() + " Item(s) ?");
                            alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    preparaExportacao();
                                }
                            });
                            alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            AlertDialog alert = alertDialog.create();
                            alert.show();

                        } else {

                            alertDialog.setMessage( getString( R.string.Erro_Dialog_Exporta1 ) );
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            AlertDialog alert = alertDialog.create();
                            alert.show();
                        }

                    }else {

                        alertDialog.setMessage( getString( R.string.Erro_Dialog_Exporta2 ) );
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }
                }else{

                    alertDialog.setMessage( getString( R.string.Aviso_Retrofit1 ) );
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<List<PermissaoExportar>> call, Throwable t) {

                alertDialog.setMessage( getString( R.string.Aviso_Retrofit1 ) );
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });

    }

    /**Metodo Responsavel por Prepara a Exportação */
    public void preparaExportacao(){

        alertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage( getString( R.string.Mensagem11 ) )
                .setCancelable(false)
                .build();

        alertDialog.show();

        for (Bem bem : bensSelecionados) {

            // Seleciona o Caminho da Foto 1 e 2
            caminhoFoto1 = new File( Environment.getExternalStorageDirectory().getAbsolutePath(),caminhoFotoPrincipal + bem.getPlaqueta() + "/" + bem.getPlaqueta() + "_1.png");
            caminhoFoto2 = new File( Environment.getExternalStorageDirectory().getAbsolutePath(),caminhoFotoPrincipal + bem.getPlaqueta() + "/" + bem.getPlaqueta() + "_2.png");

            // Verificar se a Foto 1 Existe
            if (caminhoFoto1.exists()) {

                Imagem1 = BitmapFactory.decodeFile(caminhoFoto1.toString());
                Imagem1.compress(Bitmap.CompressFormat.PNG, 100, baos1);
                byteArray1 = baos1.toByteArray();
                Imagem1.recycle();

                bem.setFoto1( Base64.encodeToString( byteArray1, Base64.DEFAULT ) );
            }

            // Verificar se a Foto 2 Existe
            if (caminhoFoto2.exists()) {

                Imagem2 = BitmapFactory.decodeFile(caminhoFoto2.toString());
                Imagem2.compress(Bitmap.CompressFormat.PNG, 100, baos2);
                byteArray2 = baos2.toByteArray();
                Imagem2.recycle();

                bem.setFoto2( Base64.encodeToString( byteArray2, Base64.DEFAULT ) );
            }
        }

        i = 1;
        bensExportados.clear();
        exportaBem( );

    }

    /**Metodo reponsavel por efetuar exportação da lista de bens para a WEB atraves da API
    */
    public void exportaBem(){

        // verificar se o contador esta maior que o tamanho do Array, Caso esteja sinal q não tem mais obejtos a serem enviados
       if( i > bensSelecionados.size() ){

            alertDialog.dismiss();
            fimExportacao();
            return ;

        }else {

           // Efetua o envio envio do objeto atraves da API
           resolvePatrimonio.enviarBens(bensSelecionados.get( i - 1 )).enqueue(new Callback<Bem>() {
               @Override
               public void onResponse(Call<Bem> call, Response<Bem> response) {

                   if (response.isSuccessful()) {

                       // Adiciona no Array List de Bem Exportados
                       bensExportados.add( bensSelecionados.get( i - 1 ) );

                       // Atualiza o Contador do Alert Dialog
                       alertDialog.setMessage("Enviando Itens: " + ( bensSelecionados.size()) + " de " +  bensSelecionados.size() );

                       // Metodo responsavel por efetuar a Exclusão da Plaqueta e das Fotos
                       excluiPlaqueta( bensSelecionados.get( i - 1 ) );

                       i++;
                       exportaBem( );
                   }
               }

               @Override
               public void onFailure(Call<Bem> call, Throwable t) {

                   i++;
                   exportaBem();
               }

           });

       }
    }

    /**Metodo que excluir a plaqueta exportada da base de dados
     @param bem Bem a ser exportador*/
    public void excluiPlaqueta(Bem bem){

        final BemDAO bemDAO = new BemDAO(getApplicationContext());

        // Deleta o Bem do Banco de Dados
        bemDAO.Deletar(bem);

        // Deleta as Fotos e a Pasta
        caminhoFoto1 = new File( Environment.getExternalStorageDirectory().getAbsolutePath(),caminhoFotoPrincipal + bem.getPlaqueta() + "/" + bem.getPlaqueta() + "_1.png");
        caminhoFoto2 = new File( Environment.getExternalStorageDirectory().getAbsolutePath(),caminhoFotoPrincipal + bem.getPlaqueta() + "/" + bem.getPlaqueta() + "_2.png");

        // Verificar se a Foto 1 Existe
        if (caminhoFoto1.exists()) {
            caminhoFoto1.delete();
        }

        // Verificar se a Foto 2 Existe
        if (caminhoFoto2.exists()) {
            caminhoFoto2.delete();
        }

        new File( Environment.getExternalStorageDirectory().getAbsolutePath(),caminhoFotoPrincipal + bem.getPlaqueta()).delete();

    }

    /**Metodo para informa a quantidade de bens exportados para o usuario*/
    public void fimExportacao() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.ic_nav_exportacao_24dp);
        alertDialog.setTitle( getString( R.string.Mensagem8 ) );

        if( bensSelecionados.size() == bensExportados.size() ) {
            alertDialog.setMessage( getString( R.string.Mensagem9 ) + String.valueOf(bensExportados.size()) + " !");
        }else  {
            alertDialog.setMessage( getString( R.string.Mensagem10 ) );
        }

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

    }

}
