package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

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
    private static final int Camera = 1;

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

    /**
     * Metodo Responsavel por a Tira a Foto 1
     */
    public void fotoUsuario(View view) {

        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);

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
