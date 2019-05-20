package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.gms.dynamic.DeferredLifecycleHelper;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.API.ResolvePatrimonio;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.BemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.CentroCustoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.EstadoConsevacaoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.FabricanteDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.ItemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.LocalizacaoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.ResponsavelDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.SecretariaDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.TipoTomboDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSharedPreferences;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.Permissao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper.RetrofitConfig;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCusto;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Configuracoes;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.EstadoConservacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Fabricante;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Item;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Localizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Responsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Secretaria;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.TipoTombo;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CadastroBem extends AppCompatActivity {

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    // Variaveis de Modelos
    private Usuario usuario;
    private Configuracoes configuracoes;
    private Bem bem;

    // Variaveis de API e Conexao
    private BemDAO bemDAO;
    private Retrofit retrofit;
    private ResolvePatrimonio resolvePatrimonio;
    private ConfiguracaoSharedPreferences preferences;

    // Listas de Controle
    private List<Bem> Bens = new ArrayList<>();
    private List<Secretaria> Secretarias = new ArrayList<>();
    private List<CentroCusto> CentroCustos = new ArrayList<>();
    private List<Localizacao> Localizacoes = new ArrayList<>();
    private List<Responsavel> Responsaveis = new ArrayList<>();
    private List<Item> Itens = new ArrayList<>();
    private List<Fabricante> Fabricantes = new ArrayList<>();
    private List<TipoTombo> TipoTombos = new ArrayList<>();
    private List<EstadoConservacao> EstadoConversacoes = new ArrayList<>();

    // Variaveis de Adapter


    // Variavel de Sistemas
    private Spinner secretariaID;
    private Spinner centrocustoID;
    private Spinner localizacaooID;
    private Spinner responsavelID;
    private Spinner itemID;
    private Spinner fabricanteID;
    private Spinner tipoTomboID;
    private Spinner estadoConservacaoID;

    private TextInputEditText Plaqueta;
    private TextInputEditText Especificacao;
    private TextInputEditText Observacao;
    private CurrencyEditText  Valor;

    private android.app.AlertDialog alertDialog;
    private AlertDialog alertDialog2;
    private ImageView Foto1ID;
    private ImageView Foto2ID;
    private Bitmap Imagem1 = null;
    private Bitmap Imagem2 = null;
    private File caminhoFoto1;
    private File caminhoFoto2;
    private ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
    private ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
    private byte[] byteArray1;
    private byte[] byteArray2;

    private static final int Camera1 = 10;
    private static final int Camera2 = 20;
    private FloatingActionButton fab;

    // Variavel de Controle
    private int edicaoBem = 0;
    private int controle;
    private String caminhoFotoPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_bem);

        retrofit = RetrofitConfig.getRetrofit();
        resolvePatrimonio = retrofit.create(ResolvePatrimonio.class);
        bemDAO = new BemDAO(getApplicationContext());

        // Vslida Permissão
        Permissao.ValidaPermissao(permissoes, this, 1);

        preferences = new ConfiguracaoSharedPreferences ( getApplicationContext() );
        usuario = preferences.recupraDadosPessoais();
        configuracoes = preferences.recupraConfiguracoes();
        caminhoFotoPrincipal = "/Resolve Patrimonio/" + String.valueOf( usuario.getClienteIDFK() ) + "/Fotos/";

        // Recebe o bem passado pela listagem caso esteja sendo iniciada pela Listagem
        bem = (Bem) getIntent().getSerializableExtra("Bem");

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(R.string.Titulo3);

        Locale locale = new Locale("pt","BR");

        Plaqueta            = findViewById(R.id.PlaquetaID);
        secretariaID        = findViewById(R.id.sp_SecretariaID);
        centrocustoID       = findViewById(R.id.sp_CentroCustoID);
        localizacaooID      = findViewById(R.id.sp_LocalizacaoID);
        responsavelID       = findViewById(R.id.sp_ResponsavelID);
        itemID              = findViewById(R.id.sp_ItemID);
        Especificacao       = findViewById(R.id.EspecificacaoID);
        fabricanteID        = findViewById(R.id.sp_FabricanteID);
        Valor               = findViewById(R.id.ValorID);
        Observacao          = findViewById(R.id.ObservacaoID);
        tipoTomboID         = findViewById(R.id.sp_TomboID);
        estadoConservacaoID = findViewById(R.id.sp_ConservacaoID);
        Foto1ID             = findViewById(R.id.iv_Foto1ID);
        Foto2ID             = findViewById(R.id.iv_Foto2ID);
        fab                 = findViewById(R.id.fabSalvar);

        Valor.setLocale(locale);

        secretariaID.setFocusable(true);
        secretariaID.setFocusableInTouchMode(true);
        secretariaID.requestFocus();

        centrocustoID.setFocusable(true);
        centrocustoID.setFocusableInTouchMode(true);
        centrocustoID.requestFocus();

        localizacaooID.setFocusable(true);
        localizacaooID.setFocusableInTouchMode(true);
        localizacaooID.requestFocus();

        responsavelID.setFocusable(true);
        responsavelID.setFocusableInTouchMode(true);
        responsavelID.requestFocus();

        itemID.setFocusable(true);
        itemID.setFocusableInTouchMode(true);
        itemID.requestFocus();

        fabricanteID.setFocusable(true);
        fabricanteID.setFocusableInTouchMode(true);
        fabricanteID.requestFocus();

        tipoTomboID.setFocusable(true);
        tipoTomboID.setFocusableInTouchMode(true);
        tipoTomboID.requestFocus();

        estadoConservacaoID.setFocusable(true);
        estadoConservacaoID.setFocusableInTouchMode(true);
        estadoConservacaoID.requestFocus();

        // Verifica se o Bem e Nulo, caso não seja e por esta vindo da Listagem e ta solicitando a Edição do Bem
        if (bem != null) {

            // Verificar se o bem e um bem da WEB, caso seja desabilita todos os campos para Edição
            if (bem.getExportado() == 1) {
                // Desabilitando todos os campos para a Edição
                desabilitaCampos();
            }

            // Carrega as informações do bem passado como paramentro
            carregaBem();

            // Atualiza a Variavel para que no momento de verificar a plaqueta informa que e um bem que pode ser editado
            edicaoBem = 1;

        } else {

            bem = new Bem();
            bem.setClienteIDFK( usuario.getClienteIDFK() );
            listaSecretaria();
            listaCentroCusto();
            listaLocalizacao();
            listaResponsavel();
            listaItem();
            listaFabricante();
            listaTipoTombo();
            listaEstadoConservacao();

        }

        // Apos Perde o Focu do EditText e verificado se a plaqueta ja existe
        Plaqueta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String p = Plaqueta.getText().toString();
                    validaPlaqueta(p);
                }
            }
        });

        // Botão para efetuar o Cadasto do Bem
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Efetuar a chamado do metodo para efetuar o cadastro
                cadastraBem();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Efetuar uma Varredura nas Permissões
        for (int permissaoResultado : grantResults) {

            // Verifica se a Permissão esta negada, para solicita permissão
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {

                // Solicita a permissão
                alertaPermissao();
            }
        }
    }

    /**
     * Metodo Reponsavel por Recebe a Imagem da Camera ou da Galeria
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            try {

                switch (requestCode) {

                    //Verificar se o Dado esta vindo da Foto 1
                    case Camera1:
                        Imagem1 = null;
                        Imagem1 = (Bitmap) data.getExtras().get("data");
                        Foto1ID.setImageBitmap(Imagem1);
                        break;

                    //Verificar se o Dado esta vindo da Foto 2
                    case Camera2:
                        Imagem2 = null;
                        Imagem2 = (Bitmap) data.getExtras().get("data");
                        Foto2ID.setImageBitmap(Imagem2);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metodo Responsavel por Alertar as permissões negadas
     */
    public void alertaPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Alerta1);
                builder.setMessage( getString( R.string.Permissao1 ) );

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /** Metodo Responsavel por Carrega Informações do Bem*/
    public void carregaBem(){

        Plaqueta.setEnabled(false);
        Plaqueta.setText(bem.getPlaqueta());
        listaSecretaria();
        listaCentroCusto();
        listaLocalizacao();
        listaResponsavel();
        listaItem();
        Especificacao.setText(bem.getEspecificacao());
        listaFabricante();
        Valor.setText(bem.getValor());
        listaTipoTombo();
        listaEstadoConservacao();
        Observacao.setText(bem.getObservacao());

        carregaFotos();
    }

    /** Metodo responsavel por Carregas as Imagem do Bem do Celular ou da WEB*/
    public void carregaFotos(){

        // Verificar se o Bem ja se encontra na WEB traz a Foto da WEB
        if (bem.getExportado() == 1) {

            // Seleciona o Caminho da Foto 1
            Picasso.with(this)
                    .load( "https://resolveconsultoria.com/patrimonio/Patrimonio/Clientes/" + bem.getClienteIDFK() +  "/" + bem.getPlaqueta() + "/" + bem.getPlaqueta() + "_1.jpg" )
                    .placeholder (R.drawable.ic_camera_24dp)
                    .error(R.drawable.ic_camera_24dp)
                    .fit()
                    .into( Foto1ID );

            // Seleciona o Caminho da Foto 2
            Picasso.with(this)
                    .load( "https://resolveconsultoria.com/patrimonio/Patrimonio/Clientes/" + bem.getClienteIDFK() +  "/" + bem.getPlaqueta() + "/" + bem.getPlaqueta() + "_2.jpg" )
                    .placeholder (R.drawable.ic_camera_24dp)
                    .error(R.drawable.ic_camera_24dp)
                    .fit()
                    .into( Foto2ID );
        }

        // Traz a Foto Local
        else{

            // Seleciona o Caminho da Foto 1
            Picasso.with(this)
                    .load( new File(Environment.getExternalStorageDirectory().getAbsolutePath(),caminhoFotoPrincipal + Plaqueta.getText().toString() + "/" + Plaqueta.getText().toString() + "_1.png"))
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder (R.drawable.ic_camera_24dp)
                    .error(R.drawable.ic_camera_24dp)
                    .into( Foto1ID );

            // Seleciona o Caminho da Foto 2
            Picasso.with(this)
                    .load( new File( Environment.getExternalStorageDirectory().getAbsolutePath(),caminhoFotoPrincipal + Plaqueta.getText().toString() + "/" + Plaqueta.getText().toString() + "_2.png"))
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder( R.drawable.ic_camera_24dp )
                    .error( R.drawable.ic_camera_24dp )
                    .into( Foto2ID );
        }

    }

    /**
     * Metodo responsavel por Lista as Secretaria cadastradas
     *
     * @param secretaria
     */
    public void listaSecretaria() {

        final SecretariaDAO secretariaDAO = new SecretariaDAO(getApplicationContext());
        final List<String> secretariasTemp = new ArrayList<>();

        Secretarias.clear();

        Secretarias = secretariaDAO.Lista();

        for (Secretaria s : Secretarias) {

            if (secretariasTemp.isEmpty()) {
                secretariasTemp.add( getString( R.string.Titulo4 ) );
            }

            secretariasTemp.add(String.valueOf(s.getSecretariaID()) + " - " + String.valueOf(s.getDescricao()).toUpperCase());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, secretariasTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        secretariaID.setAdapter(spinnerArrayAdapter);

        if (bem.getSecretariaIDFK() != null ) {

            for (int i = 0; i <= (secretariasTemp.size() - 2); i++) {

                if (Secretarias.get(i).getSecretariaID() == bem.getSecretariaIDFK()) {
                    secretariaID.setSelection(i + 1);
                }
            }
        }

        secretariaID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {

                    String sec = String.valueOf( bem.getSecretariaIDFK() ) ;
                    bem.setSecretariaIDFK(Secretarias.get(position - 1).getSecretariaID());

                    if (!sec.equals("")) {

                        if (sec != String.valueOf( bem.getSecretariaIDFK())) {
                            bem.setCentroCustoIDFK( null );
                            listaCentroCusto();
                        } else {
                            listaCentroCusto();
                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Centro Custo cadastradas
     *
     * @param controle    Paramentro de controle
     * @param centrocusto Centro Custo da plaqueta ja cadastrada
     */
    public void listaCentroCusto() {

        final CentroCustoDAO centroCustoDAO = new CentroCustoDAO(getApplicationContext());
        final List<String> centroCustoTemp = new ArrayList<>();

        CentroCustos.clear();

        CentroCustos = centroCustoDAO.Lista(bem);

        if (centroCustoTemp.isEmpty()) {
            centroCustoTemp.add( getString( R.string.Titulo5 ) );
        }

        for (CentroCusto cc : CentroCustos) {

            centroCustoTemp.add(String.valueOf(cc.getCentroCustoID()) + " - " + String.valueOf(cc.getDescricao()).toUpperCase());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, centroCustoTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        centrocustoID.setAdapter(spinnerArrayAdapter);

        if ( bem.getCentroCustoIDFK() != null ) {

            for (int i = 0; i <= ( centroCustoTemp.size() - 2 ); i++) {

                if ( CentroCustos.get(i).getCentroCustoID() == bem.getCentroCustoIDFK() ) {

                    centrocustoID.setSelection( i + 1 );

                }
            }
        }

        centrocustoID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {

                    String cc = String.valueOf( bem.getCentroCustoIDFK() ) ;
                    bem.setCentroCustoIDFK(CentroCustos.get( position - 1 ).getCentroCustoID());

                    listaLocalizacao();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista as Localizações cadastradas
     *
     * @param controle    Paramentro de controle
     * @param localizacao Localização da plaqueta ja cadastrada
     */
    public void listaLocalizacao() {

        final LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(getApplicationContext());
        final List<String> localizacaoTemp = new ArrayList<>();

        Localizacoes.clear();

        Localizacoes = localizacaoDAO.Lista(bem);

        if (localizacaoTemp.isEmpty()) {
            localizacaoTemp.add( getString( R.string.Titulo6 ) );
        }

        for (Localizacao lt : Localizacoes) {

            localizacaoTemp.add(String.valueOf(lt.getLocalizacaoID()) + " - " + String.valueOf(lt.getDescricao()).toUpperCase());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, localizacaoTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        localizacaooID.setAdapter(spinnerArrayAdapter);

        if ( bem.getLocalizacaoIDFK() != null ) {

            for ( int i = 0; i <= ( localizacaoTemp.size() - 2 ); i++ ) {

                if ( Localizacoes.get(i).getLocalizacaoID() == bem.getLocalizacaoIDFK() ) {

                    localizacaooID.setSelection( i + 1 );
                }
            }
        }

        localizacaooID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {

                    String loc = String.valueOf( bem.getLocalizacaoIDFK() ) ;
                    bem.setLocalizacaoIDFK(Localizacoes.get(position - 1).getLocalizacaoID());

                    if ( !loc.equals("") ) {

                        if ( loc != String.valueOf( bem.getLocalizacaoIDFK()) ) {
                            bem.setResponsavelIDFK( null );
                            listaResponsavel();
                        } else {
                            listaResponsavel();
                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Responsaveis cadastradas
     *
     * @param controle    Paramentro de controle
     * @param responsavel Reponsavel da plaqueta ja cadastrada
     */
    public void listaResponsavel() {

        final ResponsavelDAO responsavelDAO = new ResponsavelDAO(getApplicationContext());
        final List<String> responsavelTemp = new ArrayList<>();

        Responsaveis.clear();

        Responsaveis = responsavelDAO.Lista(bem);

        if (responsavelTemp.isEmpty()) {
            responsavelTemp.add( getString( R.string.Titulo7 ) );
        }

        for (Responsavel r : Responsaveis) {

            responsavelTemp.add(String.valueOf(String.valueOf(r.getNome()).toUpperCase()));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, responsavelTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        responsavelID.setAdapter(spinnerArrayAdapter);

        if ( bem.getResponsavelIDFK() != null ) {

            for ( int i = 0; i <= ( responsavelTemp.size() - 2 ); i++ ) {

                if ( Responsaveis.get(i).getMatricula() == bem.getResponsavelIDFK() ) {

                    responsavelID.setSelection( i + 1 );
                }
            }
        }

        responsavelID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if ( position > 0 ) {
                    bem.setResponsavelIDFK( Responsaveis.get( position - 1 ).getMatricula() );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Itens cadastradas
     *
     * @param controle Paramentro de controle
     * @param item     Item da plaqueta ja cadastrada
     */
    public void listaItem() {

        final ItemDAO itemDAO = new ItemDAO(getApplicationContext());
        final List<String> itemTemp = new ArrayList<>();

        Itens.clear();

        Itens = itemDAO.Lista(bem);

        if (itemTemp.isEmpty()) {
            itemTemp.add( getString( R.string.Titulo10 ) );
        }

        for (Item r : Itens) {

            itemTemp.add(String.valueOf(String.valueOf(r.getDescricao()).toUpperCase()));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        itemID.setAdapter(spinnerArrayAdapter);

        if (bem.getItemIDFK() != null ) {

            for (int i = 0; i <= (itemTemp.size() - 2); i++) {

                if (Itens.get(i).getItemID() == bem.getItemIDFK()) {

                    itemID.setSelection(i + 1);
                }
            }
        }

        itemID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setItemIDFK(Itens.get(position - 1).getItemID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Fabricante cadastradas
     *
     * @param controle   Paramentro de controle
     * @param fabricante Fabricante da plaqueta ja cadastrada
     */
    public void listaFabricante() {

        final FabricanteDAO fabricanteDAO = new FabricanteDAO(getApplicationContext());
        final List<String> fabricanteTemp = new ArrayList<>();

        Fabricantes.clear();

        Fabricantes = fabricanteDAO.Lista(bem);

        if (fabricanteTemp.isEmpty()) {
            fabricanteTemp.add( getString( R.string.Titulo11 ) );
        }

        for (Fabricante r : Fabricantes) {

            fabricanteTemp.add(String.valueOf(String.valueOf(r.getNome()).toUpperCase()));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fabricanteTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        fabricanteID.setAdapter(spinnerArrayAdapter);

        if (bem.getFabricanteIDFK() != null ) {

            for (int i = 0; i <= (fabricanteTemp.size() - 2); i++) {

                if (Fabricantes.get(i).getFabricanteID() == bem.getFabricanteIDFK()) {

                    fabricanteID.setSelection(i + 1);
                }
            }
        }

        fabricanteID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setFabricanteIDFK(Fabricantes.get(position - 1).getFabricanteID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Tipo Tombo cadastradas
     *
     * @param controle  Paramentro de controle
     * @param tipoTombo Tipo do Tombo da plaqueta ja cadastrada
     */
    public void listaTipoTombo() {

        final TipoTomboDAO tipoTomboDAO = new TipoTomboDAO(getApplicationContext());
        final List<String> tipotomboTemp = new ArrayList<>();

        TipoTombos.clear();

        TipoTombos = tipoTomboDAO.Lista();

        if (tipotomboTemp.isEmpty()) {
            tipotomboTemp.add( getString( R.string.Titulo12 ) );
        }

        for (TipoTombo tb : TipoTombos) {

            tipotomboTemp.add(String.valueOf(String.valueOf(tb.getDescricao()).toUpperCase()));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipotomboTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        tipoTomboID.setAdapter(spinnerArrayAdapter);

        if (bem.getTipoTomboIDFK() != null ) {

            for (int i = 0; i <= (tipotomboTemp.size() - 2); i++) {

                if (TipoTombos.get(i).getTipoTomboID() == bem.getTipoTomboIDFK()) {

                    tipoTomboID.setSelection(i + 1);
                }
            }
        }

        tipoTomboID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setTipoTomboIDFK(TipoTombos.get(position - 1).getTipoTomboID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Estado Conservacao cadastradas
     *
     * @param controle          Paramentro de controle
     * @param estadoConservacao Estado de Conservação da plaqueta ja cadastrada
     */
    public void listaEstadoConservacao() {

        final EstadoConsevacaoDAO estadoConsevacaoDAO = new EstadoConsevacaoDAO(getApplicationContext());
        final List<String> estadoConservacaoTemp = new ArrayList<>();

        EstadoConversacoes.clear();

        EstadoConversacoes = estadoConsevacaoDAO.Lista();

        if (estadoConservacaoTemp.isEmpty()) {
            estadoConservacaoTemp.add( getString( R.string.Titulo13 ) );
        }

        for (EstadoConservacao ec : EstadoConversacoes) {

            estadoConservacaoTemp.add(String.valueOf(String.valueOf(ec.getDescricao()).toUpperCase()));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estadoConservacaoTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        estadoConservacaoID.setAdapter(spinnerArrayAdapter);

        if (bem.getEstadoConservacaoIDFK() != null ) {

            for (int i = 0; i <= (estadoConservacaoTemp.size() - 2); i++) {

                if (EstadoConversacoes.get(i).getEstadoConservacaoID() == bem.getEstadoConservacaoIDFK()) {

                    estadoConservacaoID.setSelection(i + 1);
                }
            }
        }

        estadoConservacaoID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setEstadoConservacaoIDFK(EstadoConversacoes.get(position - 1).getEstadoConservacaoID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Cadastra Bem
     */
    public void cadastraBem() {

        // Antes da Prossegui e verificado todos os Campos, Caso estaja tudo ok e dados a Continuidade no cadastramento
        if (validaCampos()) {

            salvaFoto( Imagem1, bem.getPlaqueta() + "_1.png" );
            salvaFoto( Imagem2, bem.getPlaqueta() + "_2.png" );

            // Verificar se as configuração de exporta logo apos cadastra esta habilitada
            if ( configuracoes.getExporta() ){
                exportaBem();
            } else {

                // Tenta atualiza plaqueta,caso ja exista se não e feita a inserção
                if ( bemDAO.Atualizar( bem ) ) {
                    finish();
                } else {
                    //Efetua a inserção no banco de dados
                    bemDAO.Salvar( bem );
                    continuaCadastrando();
                }
            }

        } else {

            // Caso possua algum campo sem preenche e mostrado a seguinte notificação ao usuario
            if(Imagem1 == null || Imagem2 == null) {
                if (Imagem1 == null && Imagem2 == null) {
                    Snackbar.make(findViewById(R.id.fabSalvar), getString( R.string.Erro_Cadastro6 ), Snackbar.LENGTH_LONG).show();
                }else {
                    if (Imagem1 == null) {
                        Snackbar.make(findViewById(R.id.fabSalvar), getString( R.string.Erro_Cadastro7 ) , Snackbar.LENGTH_LONG).show();
                    }else
                        Snackbar.make(findViewById(R.id.fabSalvar), getString( R.string.Erro_Cadastro8 ) , Snackbar.LENGTH_LONG).show();
                }
            }else {
                Snackbar.make(findViewById(R.id.fabSalvar), getString( R.string.Erro_Cadastro9 ) , Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Metodo responsavel por Efetuar validações dos campos
     */
    public boolean validaCampos() {

        controle = 0;

        BigDecimal payment = new BigDecimal( Valor.getRawValue() ).movePointLeft(2);

        bem.setPlaqueta(Plaqueta.getText().toString());
        bem.setValor( payment.toString() );
        bem.setEspecificacao(Especificacao.getText().toString());
        bem.setObservacao(Observacao.getText().toString());
        bem.setExportado(0);


        if ( (validaPlaqueta( bem.getPlaqueta() ) ) || ( bem.getPlaqueta().isEmpty() ) ) {

            if (validaPlaqueta( bem.getPlaqueta() )) {
                Plaqueta.setError( getString( R.string.Erro_Cadastro1 ) );
            } else {
                Plaqueta.setError( getString( R.string.Erro_Cadastro2 ) );
            }
            controle = 1;
        }

        if (bem.getSecretariaIDFK() == null) {
            ((TextView) secretariaID.getSelectedView()).setError("");
            controle = 1;
        }

        if (bem.getCentroCustoIDFK() == null) {
            ((TextView) centrocustoID.getSelectedView()).setError("");
            controle = 1;
        }

        if (bem.getLocalizacaoIDFK() == null) {
            ((TextView) localizacaooID.getSelectedView()).setError("");
            controle = 1;
        }

        if (bem.getResponsavelIDFK() == null) {
            ((TextView) responsavelID.getSelectedView()).setError("");
            controle = 1;
        }

        if (bem.getItemIDFK() == null) {
            ((TextView) itemID.getSelectedView()).setError("");
            controle = 1;
        }

        if (bem.getItemIDFK() == null) {
            ((TextView) itemID.getSelectedView()).setError("");
            controle = 1;
        }

        if (bem.getEspecificacao().isEmpty()) {
            Especificacao.setError( getString( R.string.Erro_Cadastro3 ) );
            controle = 1;
        }

        if (bem.getFabricanteIDFK() == null) {
            ((TextView) fabricanteID.getSelectedView()).setError("");
            controle = 1;
        }

        if (bem.getValor().isEmpty() || String.valueOf(bem.getValor()).equals("0") ) {
            Valor.setError( getString( R.string.Erro_Cadastro4 ) );
            controle = 1;
        }

        if (bem.getEstadoConservacaoIDFK() == null) {
            ((TextView) estadoConservacaoID.getSelectedView()).setError("");
            controle = 1;
        }

        if (bem.getTipoTomboIDFK() == null) {
            ((TextView) tipoTomboID.getSelectedView()).setError("");
            controle = 1;
        }

        if (bem.getObservacao().isEmpty()) {
            Observacao.setError( getString( R.string.Erro_Cadastro5 ) );
            controle = 1;
        }

        if (Foto1ID == null) {
            controle = 1;
        }

        if (Foto2ID == null) {
            controle = 1;
        }

        if (controle == 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Metodo que Valida a Plaqueta
     * @param plaqueta Plaqueta a ser Validada
     */
    public boolean validaPlaqueta(String plaqueta) {

            Bens.clear();

            // Carrega todas as Plaqueta que ainda não foram exportadas para verifaca se a plaqueta ja existe
            Bens   = bemDAO.Lista(0);

            for (Bem b : Bens) {

                if ( plaqueta.equals(b.getPlaqueta()) && edicaoBem == 0 ) {
                    Plaqueta.setError( "Plaqueta já Existe!" );
                    return true;
                }
            }

        return false;
    }

    /**
     * Metodo Responsavel por a Tira a Foto 1
     */
    public void Foto1(View view) {

        if ( Plaqueta.getText().toString().matches( "" ) ){
            Toast.makeText( this , "Preencha Primeiro a Plaqueta" , Toast.LENGTH_SHORT ).show();
        }else {
            Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

            if ( intent.resolveActivity( getPackageManager() ) != null ) {
                startActivityForResult( intent , Camera1 );
            }
        }
    }

    /**
     * Metodo Responsavel por a Tira a Foto 2
     */
    public void Foto2(View view) {

        if ( Plaqueta.getText().toString().matches( "" ) ){
            Toast.makeText( this , "Preencha Primeiro a Plaqueta" , Toast.LENGTH_SHORT ).show();
        }else {
            Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

            if ( intent.resolveActivity( getPackageManager() ) != null ) {
                startActivityForResult( intent , Camera2 );
            }
        }
    }

    /**Metodo Responsavel por Salva as Fotos
     @param imagem Imagem em bitmap a ser salva
     @param descricao Descricão da plaqueta a ser salva
     */
    public void salvaFoto(Bitmap imagem, String descricao) {

        File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), caminhoFotoPrincipal + bem.getPlaqueta() + "/"  );

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (!dir.exists())
                dir.mkdirs();
        }

            try {

                byte[] bytes;
                FileOutputStream fos;

                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.PNG, 70, baos1);

                bytes = baos1.toByteArray();
                fos = new FileOutputStream(dir + "/" + descricao );
                fos.write(bytes);
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /** Metodo Responsavel por Desabilitar todos os Campos
     */
    public void desabilitaCampos(){

        Plaqueta.setEnabled(false);
        secretariaID.setEnabled(false);
        centrocustoID.setEnabled(false);
        localizacaooID.setEnabled(false);
        responsavelID.setEnabled(false);
        itemID.setEnabled(false);
        Especificacao.setEnabled(false);
        fabricanteID.setEnabled(false);
        Valor.setEnabled(false);
        tipoTomboID.setEnabled(false);
        estadoConservacaoID.setEnabled(false);
        Observacao.setEnabled(false);
        Foto1ID.setEnabled(false);
        Foto2ID.setEnabled(false);
        fab.hide();

    }

    /** Metodo Responsavel por pergunta ao usuario se gostaria de continua cadastrando
     */
    public void continuaCadastrando(){

        // Verificar se o usuario gostaria de contina cadastrando
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( this );
        alertDialog.setTitle( getString( R.string.Titulo3 ) );
        alertDialog.setMessage( getString( R.string.Mensagem3 ) );
        alertDialog.setCancelable( false );
        alertDialog.setPositiveButton( "Sim" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog , int which) {
                limpaCampos();
            }
        } );

        alertDialog.setNegativeButton( "Não" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog , int which) {

                finish();
            }
        } );

        AlertDialog alert = alertDialog.create();
        alert.show();

    }

    /** Metodo Responsavel por limpas todos os Campos
     */
    public void limpaCampos(){

        Plaqueta.setText("");

        bem.setSecretariaIDFK(null);
        listaSecretaria();

        bem.setCentroCustoIDFK(null);
        listaCentroCusto();

        bem.setLocalizacaoIDFK(null);
        listaLocalizacao();

        bem.setResponsavelIDFK(null);
        listaResponsavel();

        bem.setItemIDFK(null);
        listaItem();

        Especificacao.setText("");

        bem.setFabricanteIDFK(null);
        listaFabricante();

        Valor.setText("");

        bem.setTipoTomboIDFK(null);
        listaTipoTombo();

        bem.setEstadoConservacaoIDFK(null);
        listaEstadoConservacao();

        Observacao.setText("");

        Imagem1 = null;
        Imagem2 = null;

        Foto1ID.setImageResource(R.drawable.ic_camera_24dp);
        Foto2ID.setImageResource(R.drawable.ic_camera_24dp);

    }

    /** Metodo Responsavel por exporta o bem para WEB
     */
    public void exportaBem(){

        alertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage( getString( R.string.Mensagem11 ) )
                .setCancelable(false)
                .setMessage( "Exportando para WEB" )
                .build();

        alertDialog.show();

        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder( this );
        alertDialog2.setTitle( "Exportação WEB" );
        alertDialog2.setMessage( "Não foi possivel enviar a plaqueta " + bem.getPlaqueta().toString() + " para WEB, salva para ser enviada posteriomente." );
        alertDialog2.setCancelable( false );

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

        // Efetua o envio envio do objeto atraves da API
        resolvePatrimonio.enviarBens( bem ).enqueue(new Callback<Bem>() {
                @Override
                public void onResponse(Call<Bem> call, Response<Bem> response) {

                    alertDialog.dismiss();

                    if (response.isSuccessful()) {
                         // Deleta o bem no banco de dados, caso possua
                         bemDAO.Deletar( bem );
                         apagaFotos();
                         continuaCadastrando();
                    }else{
                        alertDialog2.show();
                        alertDialog2.setPositiveButton( "OK" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog , int which) {
                                    finish();
                            }
                        } );

                        AlertDialog alert = alertDialog2.create();
                        alert.show();
                    }
                }

                @Override
                public void onFailure(Call<Bem> call, Throwable t) {

                    alertDialog.dismiss();

                    //Feita tentativa de atualizar o bem , caso já exista
                    if ( bemDAO.Atualizar( bem ) ) {

                        alertDialog2.setPositiveButton( "OK" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog , int which) {
                                finish();
                            }
                        } );
                    } else {
                        //Efetua a inserção no banco de dados
                        bemDAO.Salvar( bem );

                        alertDialog2.setPositiveButton( "OK" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog , int which) {
                                continuaCadastrando();
                            }
                        } );
                    }

                    AlertDialog alert = alertDialog2.create();
                    alert.show();
                }
            });
    }

    /** Metodo Responsavel por apagar as fotos
     */
    public void apagaFotos(){

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

}
