package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

public class Configuracao extends AppCompatActivity {

    Toolbar toolbar;
    private TextView dadosPessoais;
    private Switch expotar;
    private boolean exportarEstaddo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_configuracao );

        toolbar = findViewById( R.id.toolbar );
        dadosPessoais = findViewById( R.id.tv_DadosPessoaisID );
        expotar = findViewById( R.id.sw_Exporta );

        toolbar.setTitle( getString( R.string.Nav10 ) );
        setSupportActionBar( toolbar );

        expotar.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton , boolean isChecked) {

                if ( isChecked ) {
                    Toast.makeText( Configuracao.this , "Marcou" , Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( Configuracao.this , "Desmarcou" , Toast.LENGTH_SHORT ).show();
                }
            }
        } );

    }
}
