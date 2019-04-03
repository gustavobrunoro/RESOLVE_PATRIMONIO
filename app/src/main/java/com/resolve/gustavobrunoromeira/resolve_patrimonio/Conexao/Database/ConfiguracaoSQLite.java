package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConfiguracaoSQLite extends SQLiteOpenHelper {

    private String Comando;
    public static int Version = 1;
    public static String Nome_DB = "DB_RESOLVE_PATRIMONIO";
    public static String TABELA_USUARIO = "Usuario";
    public static String TABELA_BEM = "Bem";
    public static String TABELA_SECRETARIA = "Secretaria";
    public static String TABELA_CENTROCUSTO = "CentroCusto";
    public static String TABELA_CENTROCUSTOLOCALRESPONSAVEL = "CentroCustoLocalResponsavel";
    public static String TABELA_LOCALIZACAO = "Localizacao";
    public static String TABELA_RESPONSAVEL = "Responsavel";
    public static String TABELA_ITEM = "Item";
    public static String TABELA_FABRICANTE = "Fabricante";
    public static String TABELA_TIPOTOMBO = "TipoTombo";
    public static String TABELA_ESTADOCONSERVACAO = "EstadoConservacao";

    public ConfiguracaoSQLite(Context context) {
       super(context, Nome_DB, null, Version);
      // deleteDataBase(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Cria Tabela de Usuario
        Usuario(db);

        // Cria Tabela de Bem
        Bem(db);

        // Cria Tabela de Secretaria
        Secretaria(db);

        // Cria Tabela de Centro Custo
        CentroCusto(db);

        // Cria Tabela de Centro Custo Local Responsavel
        CentroCustoLocalResponsavel(db);

        // Cria Tabela de Localização
        Localizacao(db);

        // Cria Tabela de Reponsavel
        Responsavel(db);

        // Cria Tabela de Item
        Item(db);

        // Cria Tabela de Fabricante
        Fabricante(db);

        // Cria Tabela de TipoTombo
        TipoTombo(db);

        // Cria Tabela de EstadoConservacao
        EstadoConservacao(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**Cria a Tabela de Usuario*/
    public void Usuario (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_USUARIO
                + "(   UsuarioID INTEGER PRIMARY KEY AUTOINCREMENT "
                +  " , ClienteIDFK INTEGER NULL"
                + "  , Nome TEXT NOT NULL "
                + "  , Email TEXT NOT NULL "
                + "  , Foto IMAGE NULL  )";

        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_USUARIO );
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_USUARIO );
        }

    }

    /**Cria a Tabela de Bens*/
    public void Bem (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_BEM
                + "(   BemID INTEGER PRIMARY KEY AUTOINCREMENT "
                + "  , ClienteIDFK INTEGER NULL"
                + "  , Plaqueta TEXT  NULL "
                + "  , SecretariaIDFK INTEGER  NULL "
                + "  , CentroCustoIDFK INTEGER  NULL "
                + "  , LocalizacaoIDFK INTEGER  NULL "
                + "  , ResponsavelIDFK INTEGER  NULL "
                + "  , ItemIDFK INTEGER  NULL "
                + "  , Especificacao TEXT  NULL "
                + "  , FabricanteIDFK INTEGER  NULL "
                + "  , Valor TEXT NULL "
                + "  , EstadoConservacaoIDFK INTEGER  NULL "
                + "  , TipoTomboIDFK INTEGER  NULL "
                + "  , Observacao TEXT NULL "
                + "  , Foto1 TEXT NULL "
                + "  , Foto2 TEXT NULL "
                + "  , Exportado BIT "
                + "  , UNIQUE(Plaqueta))";

        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_BEM);
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_BEM);
        }

    }

    /**Cria a Tabela de Scretaria*/
    public void Secretaria (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_SECRETARIA
                + "(   ID INTEGER PRIMARY KEY AUTOINCREMENT"
                + "  , ClienteIDFK INTEGER NOT NULL"
                + "  , SecretariaID INTEGER NOT NULL "
                + "  , Descricao TEXT NOT NULL"
                + "  , SecretarioIDFK INTEGER "
                + "  , NomeSecretario TEXT "
                + "  , Endereco TEXT "
                + "  , Numero TEXT "
                + "  , Bairro TEXT "
                + "  , Telefone TEXT "
                + "  , CodigoAnterior INTEGER "
                + "  , UNIQUE(ClienteIDFK, SecretariaID)"
                + " )";
        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_SECRETARIA);
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_SECRETARIA + " - " + e.getMessage());
        }
    }

    /**Cria a Tabela de CentroCusto*/
    public void CentroCusto (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_CENTROCUSTO
                + "(   ID INTEGER PRIMARY KEY AUTOINCREMENT "
                + "  , ClienteIDFK INTEGER NOT NULL"
                + "  , CentroCustoID INTEGER  NOT NULL"
                + "  , SecretariaIDFK INTEGER  NOT NULL"
                + "  , Descricao TEXT NOT NULL "
                + "  , ResponsavelIDFK INTEGER  "
                + "  , UNIQUE(ClienteIDFK, CentroCustoID))";

        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_CENTROCUSTO);
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_CENTROCUSTO);
        }

    }

    /**Cria a Tabela de CentroCusto Local Responsavel*/
    public void CentroCustoLocalResponsavel (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_CENTROCUSTOLOCALRESPONSAVEL
                + "(   ID INTEGER PRIMARY KEY AUTOINCREMENT "
                + "  , ClienteIDFK INTEGER "
                + "  , CentroCustoIDFK INTEGER "
                + "  , LocalizacaoIDFK INTEGER "
                + "  , Matricula INTEGER )";

        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_CENTROCUSTOLOCALRESPONSAVEL );
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_CENTROCUSTOLOCALRESPONSAVEL );
        }

    }

    /**Cria a Tabela de Localizacao*/
    public void Localizacao (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_LOCALIZACAO
                + "(   ID INTEGER PRIMARY KEY AUTOINCREMENT"
                + "  , ClienteIDFK INTEGER NOT NULL"
                + "  , LocalizacaoID INTEGER NOT NULL"
                + "  , Descricao TEXT NOT NULL"
                + "  , Complemento TEXT"
                + "  , Telefone TEXT"
                + "  , LogUsuario TEXT"
                + "  , UNIQUE(ClienteIDFK, LocalizacaoID))";

        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_LOCALIZACAO);
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_LOCALIZACAO);
        }

    }

    /**Cria a Tabela de Responsavel*/
    public void Responsavel (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_RESPONSAVEL
                + "(   ID INTEGER PRIMARY KEY AUTOINCREMENT "
                + "  , ClienteIDFK INTEGER NOT NULL "
                + "  , CentroCustoIDFK INTEGER NOT NULL"
                + "  , LocalizacaoIDFK INTEGER NOT NULL"
                + "  , Nome TEXT NOT NULL"
                + "  , Matricula INTEGER NOT NULL"
                + "  , UNIQUE(ClienteIDFK, Matricula))";

        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_RESPONSAVEL);
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_RESPONSAVEL );
        }

    }

    /**Cria a Tabela de Item*/
    public void Item (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_ITEM
                + "(   ID INTEGER PRIMARY KEY AUTOINCREMENT "
                + "  , ClienteIDFK INTEGER NOT NULL "
                + "  , ItemID INTEGER NOT NULL"
                + "  , Descricao TEXT "
                + "  , UNIQUE(ClienteIDFK, ItemID))";

        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_ITEM);
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_RESPONSAVEL );
        }

    }

    /**Cria a Tabela de Fabricante*/
    public void Fabricante (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_FABRICANTE
                + "(   ID INTEGER PRIMARY KEY AUTOINCREMENT "
                + "  , ClienteIDFK INTEGER NOT NULL "
                + "  , FabricanteID INTEGER NOT NULL"
                + "  , Nome TEXT NOT NULL"
                + "  , Descricao TEXT NOT NULL"
                + "  , UNIQUE(ClienteIDFK, FabricanteID))";

        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_FABRICANTE);
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_FABRICANTE );
        }

    }

    /**Cria a Tabela de TipoTombo*/
    public void TipoTombo (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_TIPOTOMBO
                + "(   ID INTEGER PRIMARY KEY AUTOINCREMENT "
                + "  , TipoTomboID INTEGER NOT NULL "
                + "  , Descricao TEXT "
                + "  , UNIQUE(TipoTomboID))";

        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_TIPOTOMBO);
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_TIPOTOMBO );
        }

    }

    /**Cria a Tabela de EstadoConservacao*/
    public void EstadoConservacao (SQLiteDatabase db){

        Comando = " CREATE TABLE IF NOT EXISTS " + TABELA_ESTADOCONSERVACAO
                + "(   ID INTEGER PRIMARY KEY AUTOINCREMENT "
                + "  , EstadoConservacaoID INTEGER NOT NULL "
                + "  , Descricao TEXT "
                + "  , UNIQUE(EstadoConservacaoID))";

        try {
            db.execSQL( Comando );
            Log.i("INFO_DB", "Sucesso ao Cria a Tabela " + TABELA_ESTADOCONSERVACAO);
        }
        catch (Exception e ){
            Log.i("INFO_DB", "Erro ao Cria a Tabela " + TABELA_ESTADOCONSERVACAO );
        }

    }

    public void deleteDataBase (Context context){

        context.deleteDatabase(Nome_DB);
    }

}
