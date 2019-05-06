package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.API.ResolvePatrimonio;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.Organizacao.CentroCustoActivity;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.Organizacao.LocalizacaoActivity;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.Organizacao.MapaLocalizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.Organizacao.ResponsavelActivity;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.Organizacao.SecretariaActivity;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter.AdapterPrincipal;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoFirebase;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSharedPreferences;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.Permissao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RecyclerItemClickListener;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RetrofitConfig;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import br.com.powerapps.powerimagecompress.PowerImageCompress;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String[] permissoes = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    // Variaveis de Modelos
    private Usuario usuario = new Usuario();

    // Variaveis de API e Conexao
    private Retrofit retrofit;
    private ResolvePatrimonio resolvePatrimonio;

    // Listas de Controle

    // Variaveis de Adapter
    private AdapterPrincipal adapter;
    private ConfiguracaoSharedPreferences preferences ;
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticao();

    // Variavel de Sistemas
    private Bitmap Imagem = null;
    private File caminhoFoto;

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View headerView;
    private TextView NomeUsuario;
    private CircleImageView FotoUsuario;
    private RecyclerView recyclerView;

    private MaterialSearchView searchView;
    private android.app.AlertDialog alertDialog;

    // Variavel de Controle
    private int Total;
    private static final int Camera = 1;
    private int pasta=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = RetrofitConfig.getRetrofit();
        resolvePatrimonio = retrofit.create(ResolvePatrimonio.class);
        preferences = new ConfiguracaoSharedPreferences ( getApplicationContext() );
        usuario = preferences.recupraDadosPessoais();

        toolbar           = findViewById(R.id.toolbar);
        fab               = findViewById(R.id.fab);
        drawer            = findViewById(R.id.drawer_layout);
        navigationView    = findViewById(R.id.nav_view);
        recyclerView      = findViewById(R.id.recyclePrincipalId);
        searchView        = findViewById(R.id.search_view);

        headerView        = navigationView.getHeaderView(0);
        NomeUsuario       = headerView.findViewById(R.id.NomeUsuarioID);
        FotoUsuario       = headerView.findViewById(R.id.FotoUsuarioID);

        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CadastroBem.class));
            }
        });

        // Vslida Permissão
        if (Permissao.ValidaPermissao(permissoes, this, 1) ){
            atualizaContadores();
            recuperaDadosPessoais();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener( getApplicationContext(), recyclerView,  new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(position == 0 ){

                    if ( adapter.totalBemCadastrados() == 0 ){
                        Toast.makeText(getApplicationContext(),"Não Possui Bens Cadastrados",Toast.LENGTH_LONG).show();
                    }else {
                        startActivity(new Intent(getApplicationContext(), Listagem_Item.class));
                    }

                }else{

                    if ( preferences.recuperaTotal().equals("0")){

                        Toast.makeText(getApplicationContext(),"Não Possui Bens Cadastrados",Toast.LENGTH_LONG).show();

                    }else {

                        resolvePatrimonio.totalBem(  usuario.getClienteIDFK() ).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {

                                if(response.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(), Listagem_Item_Exportados.class));
                                }else{
                                    Toast.makeText(MainActivity.this, "Não é Possivel Conectar no Servidor !", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Não é Possivel Conectar no Servidor !", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
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
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            sair();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);

        // Desativando o item de Pesquisa de Tela Principal
        menuItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_settings:
                return true;
            case R.id.menu_search:
                searchView.setMenuItem(item);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        Menu menu = navigationView.getMenu();

        if (id == R.id.nav_Inicio) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        } else if (id == R.id.nav_Consulta) {
            startActivity(new Intent(getApplicationContext(),ConsultaBem.class));
        } else if (id == R.id.nav_Tombamento) {
            startActivity(new Intent(getApplicationContext(),CadastroBem.class));
        } else if (id == R.id.nav_Exportacao) {

            if ( adapter.totalBemCadastrados() == 0 ){
                Toast.makeText(getApplicationContext(),R.string.Mensagem13,Toast.LENGTH_LONG).show();
            }else {
                startActivity(new Intent(getApplicationContext(),ExportacaoBem.class));
            }

        } else if (id == R.id.nav_Sincronizar) {
            //Snackbar.make(findViewById(R.id.CoordinatorLayoutID), "Sicronizações", Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MapaLocalizacao.class));
        } else if (id == R.id.nav_Organizacao) {
            boolean b =! menu.findItem(R.id.nav_Secretaria).isVisible();

            menu.findItem(R.id.nav_Secretaria).setVisible(b);
            menu.findItem(R.id.nav_CentroCusto).setVisible(b);
            menu.findItem(R.id.nav_LocalTrabalho).setVisible(b);
            menu.findItem(R.id.nav_Responsavel).setVisible(b);
            return true;

        } else if(id == R.id.nav_Secretaria){
            startActivity(new Intent(this,SecretariaActivity.class));
        } else if(id == R.id.nav_CentroCusto){
            startActivity(new Intent(this,CentroCustoActivity.class));
        } else if(id == R.id.nav_LocalTrabalho){
            startActivity(new Intent(this,LocalizacaoActivity.class));
        } else if(id == R.id.nav_Responsavel){
            startActivity(new Intent(this,ResponsavelActivity.class));
        } else if (id == R.id.nav_Sobre) {
            startActivity(new Intent(this,SobreActivity.class));
        }  else if (id == R.id.nav_Sair) {
            try{
                firebaseAuth.signOut();
                finish();
            }catch( Exception e){
                e.printStackTrace();
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Efetuar uma Varredura nas Permissões
        for (int permissaoResultado : grantResults) {

            // Verifica se a Permissão esta negada, para solicita permissão
            if ( permissaoResultado == PackageManager.PERMISSION_DENIED){

                // Solicita a permissão
                alertaPermissao();
            }
                        
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            try {

                switch (requestCode) {

                    //Verificar se o Dado esta vindo da Foto 1
                    case Camera:
                        Imagem = null;
                        Imagem = (Bitmap) data.getExtras().get("data");
                        FotoUsuario.setImageBitmap(Imagem);
                        salvaFoto();
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        atualizaContadores();

    }

    @Override
    protected void onResume() {
        super.onResume();

        atualizaContadores();

    }

    /**Metodo Responsavel por Alertar as permissões negadas*/
    private void alertaPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Alerta1);
        builder.setMessage(R.string.Permissao1);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {finish();}
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**Metodo para Recupera os dados do Usuario (Nome, Foto)*/
    private void recuperaDadosPessoais(){

        NomeUsuario.setText( usuario.getNome() );
        caminhoFoto  = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Resolve Patrimonio/Fotos/" + usuario.getNome() + ".png");

        // Verificar se a Foto 1 Existe
        if ( caminhoFoto.exists() ) {

            // Seta o Image View da Activity do com a Foto
            ImageView mImageView1 = PowerImageCompress.doArquivo(caminhoFoto)
                    .manterProporcao(true)
                    .exibirEm( FotoUsuario );
            // Atualiza a Imagem para se salva
            Imagem = BitmapFactory.decodeFile( String.valueOf( caminhoFoto ) );
        }

    }

    /**Metodo para atualizar os Contadores da pagina principal*/
    private void atualizaContadores(){

        //Define Layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Define Adapter
        adapter = new AdapterPrincipal(getApplicationContext(),preferences.recuperaTotal());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        carregaBemExportados();
    }

    /**Metodo para Carrega as Informações dos Bens Cadastrados das Base de dados*/
    public void carregaBemExportados(){

        alertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando Dados")
                .setCancelable(false)
                .build();

        alertDialog.show();

        resolvePatrimonio.totalBem( preferences.ClienteIDFK() )
                    .enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {

                            if (response.isSuccessful()) {

                                preferences.atualizaTotal( response.body() );
                                Total = response.body();

                                //Define Adapter
                                adapter = new AdapterPrincipal(getApplicationContext(), Total );
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                        }

                    });

        alertDialog.dismiss();
    }

    /**
     * Metodo Responsavel por a Tira a Foto 1
     */
    public void fotoUsuario(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, Camera);
        }
    }

    /**
     * Metodo Responsavel por Salva as Fotos
     */
    public void salvaFoto() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Resolve Patrimonio/Fotos/");

            if (!dir.exists())
                dir.mkdirs();
        }

        try {

            byte[] bytes;
            FileOutputStream fos;

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            Imagem.compress(Bitmap.CompressFormat.PNG, 100, baos1);

            bytes = baos1.toByteArray();
            caminhoFoto  = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Resolve Patrimonio/Fotos/" + usuario.getNome() + ".png");

            if (caminhoFoto.exists()){
                caminhoFoto.delete();
            }

            fos = new FileOutputStream(caminhoFoto);
            fos.write(bytes);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**Metodo Responsavel por Fecha o sistema quando solicitado*/
    public void sair(){

        Snackbar.make(findViewById(R.id.CoordinatorLayoutID), R.string.Mensagem4, Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.RED)
                .setAction("Sim", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finishAffinity();
                    }
                }).show();
    }

}
