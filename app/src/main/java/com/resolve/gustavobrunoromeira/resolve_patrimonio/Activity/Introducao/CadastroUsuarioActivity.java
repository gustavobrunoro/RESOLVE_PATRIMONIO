package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.Introducao;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.API.ResolvePatrimonio;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.CentroCustoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.CentroCustoLocalReponsavelDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.EstadoConsevacaoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.FabricanteDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.ItemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.LocalizacaoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.ResponsavelDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.SecretariaDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.TipoTomboDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoFirebase;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSharedPreferences;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.Base64Custom;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RetrofitConfig;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCusto;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCustoLocalResponsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.EstadoConservacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Fabricante;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Item;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Localizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Responsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Secretaria;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.TipoTombo;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CadastroUsuarioActivity extends AppCompatActivity {

    // Variaveis de Modelos
    private Usuario usuario = new Usuario();
    private static String ClienteID = "99";

    // Variaveis de API e Conexao
    private Retrofit retrofit;
    private ResolvePatrimonio resolvePatrimonio;
    private ConfiguracaoSharedPreferences preferences;
    private FirebaseAuth autenticacao;

    // Listas de Controle

    // Variaveis de Adapter

    // Variavel de Sistemas
    private EditText Nome,Email,Senha;
    private Button Cadastra;
    //ProgressDialog progressDialog = new ProgressDialog( CadastroUsuarioActivity.this);

    // Variavel de Controle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_cadastro_usuario);

        retrofit = RetrofitConfig.getRetrofit();
        resolvePatrimonio = retrofit.create(ResolvePatrimonio.class);
        preferences = new ConfiguracaoSharedPreferences ( getApplicationContext() );

        Nome     = findViewById(R.id.Cadastra_NomeID);
        Email    = findViewById(R.id.Cadastra_EmailID);
        Senha    = findViewById(R.id.Cadastra_SenhaID);
        Cadastra = findViewById(R.id.bt_Cadastrar);

        Cadastra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!Nome.getText().toString().isEmpty()){

                    if (!Email.getText().toString().isEmpty()){

                        if (!Senha.getText().toString().isEmpty()){

                            usuario.setNome(Nome.getText().toString());
                            usuario.setEmail(Email.getText().toString());
                            usuario.setSenha(Senha.getText().toString());

                            // Chama Metodo para cadastro usuario
                            CadastrarUsuario( usuario );

                        }
                        else{
                            Senha.setError("Informe a Senha!");
                        }
                    }
                    else {
                        Email.setError("Informe o Email!");
                    }
                }
                else {
                    Nome.setError("Informe o Nome!");
                }
            }
        });
    }

    /** Metodo Responsavel por Efetuar o Cadastro do Usuario
     */
    public void CadastrarUsuario(final Usuario usuario){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticao();

        final UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        builder.setDisplayName(usuario.getNome());

        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String idUsuario = Base64Custom.CodificarBase64( usuario.getEmail() );

                            usuario.setIdUsuario(idUsuario);

                            // Seta o Codigo do Usuario
                            usuario.setClienteIDFK( 99 );

                            //Salva Usuario na Web
                            usuario.SalvarUsuarioWeb( idUsuario );

                            //Salva Usuario Local
                            usuario.SalvarUsuarioLocal( getApplicationContext() );

                            //Atualia o DisplayNamae
                            autenticacao.getCurrentUser().updateProfile( builder.build() );

                            // Salva no SharedPreference
                            preferences.atualizaDadosPessoais( usuario );
                            carregaSecretaria();

                         // Verificar as Excessões que podem ocorre como email já cadastro, senha fraca, etc....
                        }else{

                            String excessao;

                            try{
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e ){
                                excessao="Digite uma senha mais forte!";
                            }catch (FirebaseAuthInvalidCredentialsException e ){
                                excessao="Email inválidoe";
                            }catch (FirebaseAuthUserCollisionException e ){
                                excessao="Email já se Encontra Cadastrado";
                            }catch (Exception e ){
                                excessao="Erro ao Cadastra usuario" + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText( CadastroUsuarioActivity.this, excessao,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**Metodo para Carrega as Informações das Secretarias das Base de dados
     * */
    public boolean carregaSecretaria(){

        final ProgressDialog progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setMax(100);
        progressDialog.setTitle("Carregando Informações do Cliente");
        progressDialog.setMessage("Carregando Secretarias....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();

        final Secretaria secretaria = new Secretaria();
        final SecretariaDAO secretariaDAO = new SecretariaDAO(getApplicationContext());

        resolvePatrimonio.recuperaSecretaria( usuario.getClienteIDFK() ,"")
                .enqueue(new Callback<List<Secretaria>>() {
                    @Override
                    public void onResponse(Call<List<Secretaria>> call, Response<List<Secretaria>> response) {

                        if (response.isSuccessful()) {

                            try {

                                final List<Secretaria> listaSec = response.body();

                                if (listaSec != null) {

                                    for (Secretaria sec : listaSec) {

                                        secretaria.setSecretariaID(sec.getSecretariaID());
                                        secretaria.setClienteIDFK(sec.getClienteIDFK());
                                        secretaria.setDescricao(sec.getDescricao());
                                        secretaria.setSecretarioIDFK(sec.getSecretarioIDFK());
                                        secretaria.setNomeSecretario(sec.getNomeSecretario());
                                        secretaria.setEndereco(sec.getEndereco());
                                        secretaria.setNumero(sec.getNumero());
                                        secretaria.setBairro(sec.getBairro());
                                        secretaria.setTelefone(sec.getTelefone());
                                        secretaria.setCodigoAnterior(sec.getCodigoAnterior());

                                        if (secretariaDAO.Salvar(secretaria)) {

                                        } else {
                                            secretariaDAO.Atualizar(secretaria);
                                        }
                                    }
                                }

                                carregaCentroCusto();

                            }catch (Exception e ){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Não é possível carregar Secretaria: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Não é possível carregar Secretaria: ", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Secretaria>> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Não é possível carregar Secretaria: " + t.getMessage() + " - " + t.toString(),Toast.LENGTH_LONG).show();
                    }
                });

        return true;
    }

    /**Metodo para Carrega as Informações do Centro Custo das Base de dados
     * */
    public boolean carregaCentroCusto(){

        final ProgressDialog progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setMax(100);
        progressDialog.setTitle("Carregando Informações do Cliente");
        progressDialog.setMessage("Carregando Centro Custo....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final CentroCusto centroCusto = new CentroCusto();
        final CentroCustoDAO centroCustoDAO = new CentroCustoDAO(getApplicationContext());

        resolvePatrimonio.recuperaCentroCusto(usuario.getClienteIDFK(),"","")
                .enqueue(new Callback<List<CentroCusto>>() {
                    @Override
                    public void onResponse(Call<List<CentroCusto>> call, Response<List<CentroCusto>> response) {

                        try {
                            final List<CentroCusto> listaCentroCusto = response.body();

                            if (listaCentroCusto != null) {

                                for (CentroCusto cc : listaCentroCusto) {

                                    centroCusto.setCentroCustoID(cc.getCentroCustoID());
                                    centroCusto.setClienteIDFK(cc.getClienteIDFK());
                                    centroCusto.setSecretariaIDFK(cc.getSecretariaIDFK());
                                    centroCusto.setDescricao(cc.getDescricao());
                                    centroCusto.setResponsavelIDFK(cc.getResponsavelIDFK());

                                    if (centroCustoDAO.Salvar(centroCusto)) {

                                    } else {
                                        centroCustoDAO.Atualizar(centroCusto);
                                    }
                                }
                                carregaCentroCustoLocalResponsavel();
                            }
                        }catch (Exception e ){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Não e possivel carrega Centro Custo: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CentroCusto>> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Não e possivel carrega Ceentro Custo: " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        return true;

    }

    /**Metodo para Carrega as Informações do Centro Custo Local Responsavel das Base de dados
     * */
    public boolean carregaCentroCustoLocalResponsavel(){


        final ProgressDialog progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setMax(100);
        progressDialog.setTitle("Carregando Informações do Cliente");
        progressDialog.setMessage("Carregando Centro Custo Local Responsavel....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final CentroCustoLocalResponsavel centroCustoLocalResponsavel = new CentroCustoLocalResponsavel();
        final CentroCustoLocalReponsavelDAO centroCustoLocalReponsavelDAO = new CentroCustoLocalReponsavelDAO(getApplicationContext());

        resolvePatrimonio.recuperaCentroCustoLocalResponsavel(usuario.getClienteIDFK(),"","")
                .enqueue(new Callback<List<CentroCustoLocalResponsavel>>() {
                    @Override
                    public void onResponse(Call<List<CentroCustoLocalResponsavel>> call, Response<List<CentroCustoLocalResponsavel>> response) {

                        try {
                            final List<CentroCustoLocalResponsavel> listaCentroCustoLocalResponsavel = response.body();

                            if (listaCentroCustoLocalResponsavel != null) {

                                for (CentroCustoLocalResponsavel cclt : listaCentroCustoLocalResponsavel) {

                                    centroCustoLocalResponsavel.setClienteIDFK(cclt.getClienteIDFK());
                                    centroCustoLocalResponsavel.setCentroCustoIDFK(cclt.getCentroCustoIDFK());
                                    centroCustoLocalResponsavel.setLocalizacaoIDFK(cclt.getLocalizacaoIDFK());
                                    centroCustoLocalResponsavel.setMatricula(cclt.getMatricula());

                                    if (centroCustoLocalReponsavelDAO.Salvar(centroCustoLocalResponsavel)) {

                                    } else {
                                        centroCustoLocalReponsavelDAO.Atualizar(centroCustoLocalResponsavel);
                                    }
                                }
                                carregaLocalizacao();
                            }
                        }catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Não e possivel carrega Centro Custo Local Responsavel: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CentroCustoLocalResponsavel>> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Não e possivel carrega Centro Custo Local Responsavel: " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        return true;

    }

    /**Metodo para Carrega as Informações das Localizações das Base de dados
     * */
    public boolean carregaLocalizacao(){

        final ProgressDialog progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setMax(100);
        progressDialog.setTitle("Carregando Informações do Cliente");
        progressDialog.setMessage("Carregando Localizações....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final Localizacao localizacao = new Localizacao();
        final LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(getApplicationContext());

        resolvePatrimonio.recuperaLocalizacao(usuario.getClienteIDFK(),"")
                .enqueue(new Callback<List<Localizacao>>() {
                    @Override
                    public void onResponse(Call<List<Localizacao>> call, Response<List<Localizacao>> response) {

                        try {
                            final List<Localizacao> listaLocalizacao = response.body();

                            if (listaLocalizacao != null) {

                                for (Localizacao lt : listaLocalizacao) {

                                    localizacao.setClienteIDFK(lt.getClienteIDFK());
                                    localizacao.setLocalizacaoID(lt.getLocalizacaoID());
                                    localizacao.setDescricao(lt.getDescricao());
                                    localizacao.setComplemento(lt.getComplemento());
                                    localizacao.setTelefone(lt.getTelefone());
                                    localizacao.setLogUsuario(lt.getLogUsuario());

                                    if (localizacaoDAO.Salvar(localizacao)) {

                                    } else {
                                        localizacaoDAO.Atualizar(localizacao);
                                    }
                                }
                                carregaResponsavel();
                            }
                        }catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Não e possivel carrega Localização: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Localizacao>> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Não e possivel carrega Localização: " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        return true;

    }

    /**Metodo para Carrega as Informações dos Responsaveis das Base de dados
     * */
    public boolean carregaResponsavel(){

        final ProgressDialog progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setMax(100);
        progressDialog.setTitle("Carregando Informações do Cliente");
        progressDialog.setMessage("Carregando Responsaveis....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final Responsavel responsavel = new Responsavel();
        final ResponsavelDAO responsavelDAO = new ResponsavelDAO(getApplicationContext());

        resolvePatrimonio.recuperaResponsavel(usuario.getClienteIDFK(),"")
                .enqueue(new Callback<List<Responsavel>>() {
                    @Override
                    public void onResponse(Call<List<Responsavel>> call, Response<List<Responsavel>> response) {

                        try {
                            final List<Responsavel> listaResponsavel = response.body();

                            if (listaResponsavel != null) {

                                for (Responsavel r : listaResponsavel) {

                                    responsavel.setClienteIDFK(r.getClienteIDFK());
                                    responsavel.setCentroCustoIDFK(r.getCentroCustoIDFK());
                                    responsavel.setLocalizacaoIDFK(r.getLocalizacaoIDFK());
                                    responsavel.setNome(r.getNome());
                                    responsavel.setMatricula(r.getMatricula());

                                    if (responsavelDAO.Salvar(responsavel)) {

                                    } else {
                                        responsavelDAO.Atualizar(responsavel);
                                    }
                                }
                                carregaItem();
                            }
                        }catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Não e possivel carrega Responsavel: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Responsavel>> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Não e possivel carrega Responsavel: " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        return true;

    }

    /**Metodo para Carrega as Informações dos Itens das Base de dados
     * */
    public boolean carregaItem(){


        final ProgressDialog progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setMax(100);
        progressDialog.setTitle("Carregando Informações do Cliente");
        progressDialog.setMessage("Carregando Itens....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final Item item = new Item();
        final ItemDAO itemDAO = new ItemDAO(getApplicationContext());

        resolvePatrimonio.recuperaItem(usuario.getClienteIDFK(),"")
                .enqueue(new Callback<List<Item>>() {
                    @Override
                    public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

                        try {
                            final List<Item> listaItem = response.body();

                            if (listaItem != null) {

                                for (Item i : listaItem) {

                                    item.setClienteIDFK(i.getClienteIDFK());
                                    item.setItemID(i.getItemID());
                                    item.setDescricao(i.getDescricao());

                                    if (itemDAO.Salvar(item)) {

                                    } else {
                                        itemDAO.Atualizar(item);
                                    }
                                }
                                carregaFabricante();
                            }
                        }catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Não e possivel carrega Itens: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Item>> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Não e possivel carrega Itens: " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        return true;

    }

    /**Metodo para Carrega as Informações dos Fabricante das Base de dados
     * */
    public boolean carregaFabricante(){


        final ProgressDialog progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setMax(100);
        progressDialog.setTitle("Carregando Informações do Cliente");
        progressDialog.setMessage("Carregando Fabricante....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final Fabricante fabricante = new Fabricante();
        final FabricanteDAO fabricanteDAO = new FabricanteDAO(getApplicationContext());

        resolvePatrimonio.recuperaFabricante(usuario.getClienteIDFK(),"")
                .enqueue(new Callback<List<Fabricante>>() {
                    @Override
                    public void onResponse(Call<List<Fabricante>> call, Response<List<Fabricante>> response) {

                        try {
                            final List<Fabricante> listaFabricante = response.body();

                            if (listaFabricante != null) {

                                for (Fabricante f : listaFabricante) {

                                    fabricante.setClienteIDFK(f.getClienteIDFK());
                                    fabricante.setFabricanteID(f.getFabricanteID());
                                    fabricante.setNome(f.getNome());
                                    fabricante.setDescricao(f.getDescricao());

                                    if (fabricanteDAO.Salvar(fabricante)) {

                                    } else {
                                        fabricanteDAO.Atualizar(fabricante);
                                    }
                                }
                                carregaTipoTombo();
                            }
                        }catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Não e possivel carrega Fabricante: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Fabricante>> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Não e possivel carrega Fabricante: " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        return true;

    }

    /**Metodo para Carrega as Informações dos Tipo do Tombo das Base de dados
     * */
    public boolean carregaTipoTombo(){


        final ProgressDialog progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setMax(100);
        progressDialog.setTitle("Carregando Informações do Cliente");
        progressDialog.setMessage("Carregando Tipo Tombo....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final TipoTombo tipoTombo = new TipoTombo();
        final TipoTomboDAO tipoTomboDAO = new TipoTomboDAO(getApplicationContext());

        resolvePatrimonio.recuperaTipoTombo()
                .enqueue(new Callback<List<TipoTombo>>() {
                    @Override
                    public void onResponse(Call<List<TipoTombo>> call, Response<List<TipoTombo>> response) {

                        try {
                            final List<TipoTombo> listaTipoTombo = response.body();

                            if (listaTipoTombo != null) {

                                for (TipoTombo tt : listaTipoTombo) {

                                    tipoTombo.setTipoTomboID(tt.getTipoTomboID());
                                    tipoTombo.setDescricao(tt.getDescricao());

                                    if (tipoTomboDAO.Salvar(tipoTombo)) {

                                    } else {
                                        tipoTomboDAO.Atualizar(tipoTombo);
                                    }
                                }
                                carregaEstadoConservacao();
                            }
                        }catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Não e possivel carrega Tipo Tombo: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TipoTombo>> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Não e possivel carrega Tipo Tombo: " + t.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

        return true;

    }

    /**Metodo para Carrega as Informações dos Estado de Conservacao das Base de dados
     * */
    public boolean carregaEstadoConservacao(){


        final ProgressDialog progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setMax(100);
        progressDialog.setTitle("Carregando Informações do Cliente");
        progressDialog.setMessage("Carregando Estado Conservacao....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final EstadoConservacao estadoConservacao = new EstadoConservacao();
        final EstadoConsevacaoDAO estadoConsevacaoDAO = new EstadoConsevacaoDAO(getApplicationContext());

        resolvePatrimonio.recuperaEstadoConservacao()
                .enqueue(new Callback<List<EstadoConservacao>>() {
                    @Override
                    public void onResponse(Call<List<EstadoConservacao>> call, Response<List<EstadoConservacao>> response) {

                        try {
                            final List<EstadoConservacao> listaEstadoConservacao = response.body();

                            if (listaEstadoConservacao != null) {

                                for (EstadoConservacao ec : listaEstadoConservacao) {

                                    estadoConservacao.setEstadoConservacaoID(ec.getEstadoConservacaoID());
                                    estadoConservacao.setDescricao(ec.getDescricao());

                                    if (estadoConsevacaoDAO.Salvar(estadoConservacao)) {

                                    } else {
                                        estadoConsevacaoDAO.Atualizar(estadoConservacao);
                                    }
                                }

                                progressDialog.dismiss();
                                finish();
                            }
                        }catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Não e possivel carrega Estado de Conservação: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<EstadoConservacao>> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Não e possivel carrega Estado COnservação: " + t.getMessage(),Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

        return true;

    }

}
