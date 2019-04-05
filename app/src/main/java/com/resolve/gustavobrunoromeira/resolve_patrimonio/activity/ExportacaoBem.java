package com.resolve.gustavobrunoromeira.resolve_patrimonio.activity;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.API.ResolvePatrimonio;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterBem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterExportacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.BemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RetrofitConfig;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.PermissaoExportar;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExportacaoBem extends AppCompatActivity {

    private String ClienteIDFK = "99";

    private List<Bem> bens = new ArrayList<>();
    private List<Bem> bensSelecionados = new ArrayList<>();
    private List<Bem> bensExportados = new ArrayList<>();
    private Bem bem = new Bem();
    private AlertDialog alertDialog;

    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private AdapterExportacao adapter;

    private String caminhoFotoPrincipal = "/Resolve Patrimonio/" + ClienteIDFK + "/Fotos/";
    private File caminhoFoto1;
    private File caminhoFoto2;
    private Bitmap Imagem1;
    private Bitmap Imagem2;
    private ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
    private ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
    private byte[] byteArray1;
    private byte[] byteArray2;
    private Retrofit retrofit;
    private ResolvePatrimonio resolvePatrimonio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportacao_bem);

        recyclerView = findViewById(R.id.recycleExportacaoId);
        toolbar      = findViewById(R.id.toolbar);
        searchView   = findViewById(R.id.search_view);
        retrofit     = RetrofitConfig.getRetrofit();

        toolbar.setTitle(R.string.Nav4);
        setSupportActionBar(toolbar);

        consultaLocal();
        resolvePatrimonio = retrofit.create(ResolvePatrimonio.class);

        //Configurando o Recycle View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Configurando o Adapter
        adapter = new AdapterExportacao(bens,this);
        recyclerView.setAdapter(adapter);

        // if(savedInstanceState != null ){
        //
        //       bensSelecionados.clear();
        //       bensSelecionados = (List<Bem>) savedInstanceState.getSerializable("BensSelecionados");
        //       Log.i("ControleLogte", "Recuperou: "  + bensSelecionados.size() );
        //
        //}

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

        Log.i("ControleLogte", "confirmaExportacao: " + adapter.ListaSelecionados().size() );

        resolvePatrimonio.recuperaPermissao( ClienteIDFK ).enqueue(new Callback<List<PermissaoExportar>>() {
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
                                public void onClick(DialogInterface dialog, int which) { exportarBem(); }
                            });
                            alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            AlertDialog alert = alertDialog.create();
                            alert.show();

                        } else {

                            alertDialog.setMessage("Nenhum Item Selecionado !");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            AlertDialog alert = alertDialog.create();
                            alert.show();
                        }

                    }else {

                        alertDialog.setMessage("Necessario Permissão para Exportar!");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }
                }else{

                    alertDialog.setMessage("Não e Possivel Conectar ao Servidor!");
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

                alertDialog.setMessage("Não e Possivel Conectar ao Servidor!");
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

    /**Metodo que Exporta os Bens para a WEB atraves da API*/
    public void exportarBem(){

//        alertDialog = new SpotsDialog.Builder()
//                .setContext(this)
//                .setMessage("Preparando Exportação")
//                .setCancelable(false)
//                .build();
//
//        alertDialog.show();

        for (final Bem bem : bensSelecionados) {

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

            resolvePatrimonio.enviarBens( bem ).enqueue(new Callback<Bem>() {
                @Override
                public void onResponse(Call<Bem> call, Response<Bem> response) {

                      if(response.isSuccessful()){

                        bensExportados.add( bem );

                        //alertDialog.setMessage("Enviando Itens: " + ( bensExportados.size() + 1) + " de " +  bensSelecionados.size() );

                        // Chama o Metodo responsavel por efetuar a Exclusão da Plaqueta e das Fotos
                        excluiPlaqueta( bem );

                     }
                }

                @Override
                public void onFailure(Call<Bem> call, Throwable t) {

                    //alertDialog.dismiss();

                    Log.i("ControleLogte","Erro 2: " + t.getMessage() );
                }
            });

        }
    }

    /**Metodo que Excluir a Plaqueta exportada da base de Dados*/
    public void excluiPlaqueta(Bem bem){

        final BemDAO bemDAO = new BemDAO(getApplicationContext());

        // Deleta o Bem da Lista de Item a Serem Exportados
        for(Bem b: bens) {

            if( b.getClienteIDFK().equals(bem.getClienteIDFK()) && b.getPlaqueta().equals(bem.getPlaqueta())) {

                Log.i("ControleLogte", "Remove: " + b.getPlaqueta());

                bens.remove(b);
            }
        }

        // Deleta o Bem da Lista de Pré Selecionados para Exportação
        for(Bem b: bensSelecionados) {

            if( b.getClienteIDFK().equals(bem.getClienteIDFK()) && b.getPlaqueta().equals(bem.getPlaqueta())) {
                bensSelecionados.remove(b);
            }
        }

        // Deleta o Bem do Banco de Dados
//        bemDAO.Deletar(bem);
//
//        // Deleta as Fotos
//        caminhoFoto1.delete();
//        caminhoFoto2.delete();

    }

    /**Metodo para Informa a quantidade de Bens Exportados para o Usuario*/
    public void exportados() {

        alertDialog.dismiss();

        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.ic_nav_exportacao_24dp);
        alertDialog.setTitle("Exportação de Bens");

        if( bensSelecionados.size() == bensExportados.size() ) {
            alertDialog.setMessage("Total de Itens Exportados " + String.valueOf(bensExportados.size()) + " !");
        }else  {
            alertDialog.setMessage("Falha ao Enviar todos os Itens !");
        }

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        android.support.v7.app.AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void teste(Bem b) {

        resolvePatrimonio.enviarBensteste(b)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bem bem) {


                    }
                });

//        resolvePatrimonio.enviarBensTeste("99")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<Bem>>() {
//
//                    @Override
//                    public void onCompleted() {
//                        Log.i("TesteObservador","onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("TesteObservador",e.toString());
//                    }
//
//                    @Override
//                    public void onNext(List<Bem> bens ) {
//
//                        List<Bem> lb = new ArrayList<>();
//
//                        lb = bens;
//
//                        for(Bem b:lb){
//
//                            Log.i("TesteObservador","plaqueta: " + b.getPlaqueta());
//
//                        }
//
//                        Log.i("TesteObservador","onNext");
//
//                    }
//                });

    }

}
