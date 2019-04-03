package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth firebaseAuth ;
    private static DatabaseReference databaseReference;

    /**Metodos Responsavel Retorna Instacia de Autenticação do Firebase
     @return  FirebaseAuth*/
    public static FirebaseAuth getFirebaseAutenticao(){

        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            //firebaseAuth.signOut();
        }
        return firebaseAuth;
    }

    /**Metodos Responsavel Retorna Instacia de Database do Firebase
     @return  DatabaseReference*/
    public static DatabaseReference getDatabaseReference(){

        if( databaseReference == null ){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

}
