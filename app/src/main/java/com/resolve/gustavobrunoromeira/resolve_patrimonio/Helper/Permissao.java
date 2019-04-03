package com.resolve.gustavobrunoromeira.resolve_patrimonio.Helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    /**Metodos Responsavel por Valida Permissões Necessarias
     @return  Boolean*/
    public static Boolean ValidaPermissao(String[] permissoes, Activity activity, int requestCode){

        if(Build.VERSION.SDK_INT >= 23){

            List<String> lista = new ArrayList<>();

            for(String permissao : permissoes){

                Boolean tem = ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;
                if(!tem){
                    lista.add(permissao);
                }
            }

            String[] novaspermissoes = new String[ lista.size() ];
            lista.toArray( novaspermissoes );

            // Verifica se existem permisões a serem solicitada
            if (lista.isEmpty()){
                return true;
            }else{
                //Solicita a Permissão
                ActivityCompat.requestPermissions(activity,novaspermissoes,requestCode);
            }

        }

        return true;
    }

}

