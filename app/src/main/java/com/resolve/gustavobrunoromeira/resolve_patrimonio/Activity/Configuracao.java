package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSharedPreferences;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Configuracoes;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.io.File;

import br.com.powerapps.powerimagecompress.PowerImageCompress;
import de.hdodenhof.circleimageview.CircleImageView;

public class Configuracao extends AppCompatActivity {

    // Variaveis de Modelos
    private Usuario usuario = new Usuario();
    private Configuracoes configuracoes = new Configuracoes();

    // Variaveis de API e Conexao
    private ConfiguracaoSharedPreferences preferences ;

    // Listas de Controle

    // Variaveis de Adapter

    // Variavel de Sistemas
    Toolbar toolbar;
    private TextView NomeUsuario;
    private CircleImageView FotoUsuario;
    private Switch expotar;
    private boolean exportarEstaddo;
    private File caminhoFoto;
    private Bitmap Imagem = null;

    // Variavel de Controle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_configuracao );

        preferences = new ConfiguracaoSharedPreferences ( getApplicationContext() );
        usuario = preferences.recupraDadosPessoais();
        configuracoes = preferences.recupraConfiguracoes();

        toolbar       = findViewById( R.id.toolbar );
        expotar       = findViewById( R.id.sw_Exporta );
        NomeUsuario   = findViewById(R.id.tv_DadosPessoaisID);
        FotoUsuario   = findViewById( R.id.iv_FotoConfiguracaoID );

        toolbar.setTitle( getString( R.string.Nav10 ) );
        setSupportActionBar( toolbar );

        recuperaDadosPessoais();

        if( configuracoes != null ){

            if ( configuracoes.getExporta() ){
                expotar.setChecked( true );
            }

        }else{
            configuracoes = new Configuracoes();
        }

        expotar.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton , boolean isChecked) {

                if ( isChecked ) {
                    configuracoes.setExporta( true );
                } else {
                    configuracoes.setExporta( false );
                }

                preferences.atualizaConfiguracoes( configuracoes );

            }
        } );
    }

    /**Metodo para recupera os dados do usuario (Nome, Foto)*/
    private void recuperaDadosPessoais(){

        NomeUsuario.setText( usuario.getNome() );
        caminhoFoto  = new File( Environment.getExternalStorageDirectory().getAbsolutePath(), "/Resolve Patrimonio/Fotos/" + usuario.getNome() + ".png");

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

}
