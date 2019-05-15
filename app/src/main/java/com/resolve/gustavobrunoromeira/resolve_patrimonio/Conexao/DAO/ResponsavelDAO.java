package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Interfaces.IResponsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Responsavel;

import java.util.ArrayList;
import java.util.List;

public class ResponsavelDAO implements IResponsavel {

    private SQLiteDatabase Escreve,Ler;
    private int clienteIDFK = 99;
    private long retorno;

    public ResponsavelDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    /**Metodos Responsavel Por Salvar o Responsavel no Banco de Dados
     @param responsavel
     @return Boolean */
    @Override
    public boolean Salvar(Responsavel responsavel) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",responsavel.getClienteIDFK());
        Valor.put("CentroCustoIDFK",responsavel.getCentroCustoIDFK());
        Valor.put("LocalizacaoIDFK",responsavel.getLocalizacaoIDFK());
        Valor.put("Nome",responsavel.getNome());
        Valor.put("Matricula",responsavel.getMatricula());

        try{

            retorno = Escreve.insert(ConfiguracaoSQLite.TABELA_RESPONSAVEL,null, Valor);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Atualizar o Responsavel no Banco de Dados
     @param responsavel
     @return Boolean */
    @Override
    public boolean Atualizar(Responsavel responsavel) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",responsavel.getClienteIDFK());
        Valor.put("CentroCustoIDFK",responsavel.getCentroCustoIDFK());
        Valor.put("LocalizacaoIDFK",responsavel.getLocalizacaoIDFK());
        Valor.put("Nome",responsavel.getNome());

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf(responsavel.getMatricula())};
            retorno = Escreve.update(ConfiguracaoSQLite.TABELA_RESPONSAVEL, Valor ,"ClienteIDFK = ? AND Matricula = ?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Deletar o Responsavel no Banco de Dados
     @param responsavel
     @return Boolean */
    @Override
    public boolean Deletar(Responsavel responsavel) {

        try{
            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( responsavel.getMatricula() )};
            retorno = Escreve.delete(ConfiguracaoSQLite.TABELA_RESPONSAVEL,"ClienteIDFK = ? AND Matricula = ?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Lista o Responsavel no Banco de Dados
     @param bem
     @return Lista de Responsaveis pesquisados */
    @Override
    public List<Responsavel> Lista(Bem bem) {

        String Nome;
        int  ClienteIDFK, CentroCustoIDFK, LocalizacaoIDFK, Matricula;
        List<Responsavel> responsaveis = new ArrayList<>();

        String sql;
        String[] args;
        Cursor cursor;

        if(bem.getCentroCustoIDFK() != null){

            args = new String[]{ String.valueOf( clienteIDFK ), String.valueOf(bem.getCentroCustoIDFK())};

            sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_RESPONSAVEL + " WHERE ClienteIDFK = ? AND CentroCustoIDFK = ? ORDER BY Nome;";
            cursor = Ler.rawQuery(sql,args);

            if(bem.getLocalizacaoIDFK() != null){

                args = new String[]{ String.valueOf( clienteIDFK ), String.valueOf(bem.getCentroCustoIDFK()) , String.valueOf(bem.getLocalizacaoIDFK()) };

                sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_RESPONSAVEL + " WHERE ClienteIDFK = ? AND CentroCustoIDFK = ? AND LocalizacaoIDFK = ? ORDER BY Nome;";
                cursor = Ler.rawQuery(sql,args);
            }
        }else{
            args = new String[] { String.valueOf( clienteIDFK )};

            sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_RESPONSAVEL + " WHERE ClienteIDFK = ?;";
            cursor = Ler.rawQuery(sql,args);
        }

        while (cursor.moveToNext()){

            Responsavel responsavel = new Responsavel();

            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            CentroCustoIDFK = cursor.getInt(cursor.getColumnIndex("CentroCustoIDFK"));
            LocalizacaoIDFK = cursor.getInt(cursor.getColumnIndex("LocalizacaoIDFK"));
            Nome = cursor.getString(cursor.getColumnIndex("Nome"));
            Matricula = cursor.getInt(cursor.getColumnIndex("Matricula"));

            responsavel.setClienteIDFK(ClienteIDFK);
            responsavel.setCentroCustoIDFK(CentroCustoIDFK);
            responsavel.setLocalizacaoIDFK(LocalizacaoIDFK);
            responsavel.setNome(Nome);
            responsavel.setMatricula(Matricula);

            responsaveis.add(responsavel);

          }
        return responsaveis;
    }

    /**Metodos Responsavel Por Pesquisa o Responsavel no Banco de Dados
     @param responsavel
     @return Lista de responsaveis pesquisados */
    public List<Responsavel> Pesquisa(String responsavel) {

        String Nome;
        int  ClienteIDFK, CentroCustoIDFK, LocalizacaoIDFK, Matricula;
        List<Responsavel> responsaveis = new ArrayList<>();

        Cursor cursor;
        String sql;
        String[] args = {"%" + responsavel + "%"};

        responsavel = responsavel.toUpperCase();

        if (responsavel.length() > 0 ) {

            if (responsavel.matches(".*[A-Z].*")) {

                sql = " SELECT * FROM " + ConfiguracaoSQLite.TABELA_RESPONSAVEL + " WHERE Upper(Nome) LIKE ? ;";

            } else {
                sql = " SELECT * FROM " + ConfiguracaoSQLite.TABELA_RESPONSAVEL + " WHERE Matricula LIKE ? ;";
            }

            cursor = Ler.rawQuery(sql,args);

        }else  {

            sql = " SELECT *  FROM " + ConfiguracaoSQLite.TABELA_RESPONSAVEL + " ;";

            cursor = Ler.rawQuery(sql,null);
        }

        while (cursor.moveToNext()){

            Responsavel responsavel1 = new Responsavel();

            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            CentroCustoIDFK = cursor.getInt(cursor.getColumnIndex("CentroCustoIDFK"));
            LocalizacaoIDFK = cursor.getInt(cursor.getColumnIndex("LocalizacaoIDFK"));
            Nome = cursor.getString(cursor.getColumnIndex("Nome"));
            Matricula = cursor.getInt(cursor.getColumnIndex("Matricula"));

            responsavel1.setClienteIDFK(ClienteIDFK);
            responsavel1.setCentroCustoIDFK(CentroCustoIDFK);
            responsavel1.setLocalizacaoIDFK(LocalizacaoIDFK);
            responsavel1.setNome(Nome);
            responsavel1.setMatricula(Matricula);

            responsaveis.add(responsavel1);

        }
        return responsaveis;
    }

}
