package com.resolve.gustavobrunoromeira.resolve_patrimonio.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import mehdi.sakout.aboutpage.AboutPage;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        //setContentView(R.layout.activity_sobre);

        String descricao = "A Resolve é uma empresa especializada em consultoria de gestão pública e locação de softwares e atua no estado da Bahia há mais de 10 anos.\n" +
                "\n" +
                "Fundada pelo contador Ricardo Teixeira, a Resolve se posicionou ao longo dos anos como parceira da gestão pública na busca por soluções simples e eficientes a fim de construir uma administração cada vez mais eficaz e transparente para os municípios.\n" +
                "\n" +
                "Com uma marca forte e consolidada entre seus clientes, a Resolve tem como meta a excelência em atendimento, proporcionando aos gestores municipais maior clareza, facilidade no acompanhamento das ações de cada órgão e principalmente mobilidade na visualização das informações necessárias para as tomadas de decisões.\n" +
                "\n" +
                "Com uma equipe altamente capacitada, a Resolve proporciona em seu atendimento um suporte moderno e eficiente garantindo aos usuários e gestores tranquilidade e confiança em todos os seus produtos e serviços.";

        View sobre = new AboutPage(this).isRTL(false)
                                                .setImage(R.drawable.logo)
                                                .setDescription(descricao)
                                                .addGroup("Fale Conosco")
                                                .addEmail("programacao@resolveconsultoria.com","Envie um Email")
                                                .addWebsite("https://resolveconsultoria.com/")
//                                                .addFacebook("gustavobrunoro")
//                                                .addTwitter("gustavobrunoro")
//                                                .addInstagram("gustavobrunoro")
                                                .create();

        setContentView(sobre);
    }

}
