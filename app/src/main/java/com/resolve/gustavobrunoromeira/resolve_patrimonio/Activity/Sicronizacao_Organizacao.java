package com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Service.MyFirebaseMessagingService;

public class Sicronizacao_Organizacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sicronizacao__organizacao );


        MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();

        //myFirebaseMessagingService.recuperaToken();

    }
}
