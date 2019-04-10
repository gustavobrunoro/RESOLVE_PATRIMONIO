package com.resolve.gustavobrunoromeira.resolve_patrimonio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.MainActivity;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.introducao.CadastroUsuarioActivity;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Activity.introducao.LoginActivity;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class IntroducaoActivity extends IntroActivity {

    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticao();

    @Override
    protected void onStart() {
        super.onStart();
        autenticacao();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonNextVisible(false);
        setButtonBackVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_blue_light)
                .fragment(com.resolve.gustavobrunoromeira.resolve_patrimonio.R.layout.intro_1)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_blue_light)
                .fragment(com.resolve.gustavobrunoromeira.resolve_patrimonio.R.layout.intro_2)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_blue_light)
                .fragment(com.resolve.gustavobrunoromeira.resolve_patrimonio.R.layout.intro_3)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_blue_light)
                .fragment(com.resolve.gustavobrunoromeira.resolve_patrimonio.R.layout.intro_4)
                .canGoForward(true)
                .canGoForward(true)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_blue_light)
                .fragment(com.resolve.gustavobrunoromeira.resolve_patrimonio.R.layout.intro_cadastro_login)
                .canGoForward(true)
                .canGoForward(false)
                .build());
    }

    /**Metodo Reponsavel poer verificar se o Usuario ja se Encontra Autenticado */
    protected void autenticacao() {

        if (firebaseAuth.getCurrentUser() != null ){
            startActivity(new Intent(IntroducaoActivity.this, MainActivity.class));
        }
    }

    /**Metodo Reponsavel Cadastrar Usuario */
    public void btCadastra(View view){

        startActivity(new Intent(getApplicationContext(), CadastroUsuarioActivity.class));
    }

    /**Metodo Reponsavel por Efetuar Login do Usuario */
    public void btLogin(View view){

        startActivity(new Intent(this, LoginActivity.class));
    }

}
