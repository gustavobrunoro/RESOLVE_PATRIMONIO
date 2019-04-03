package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces.ILocalizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Localizacao;

import java.util.ArrayList;
import java.util.List;

public class LocalizacaoDAO implements ILocalizacao {

    private SQLiteDatabase Escreve,Ler;
    private int clienteIDFK = 99;

    public LocalizacaoDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    @Override
    public boolean Salvar(Localizacao localizacao) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",localizacao.getClienteIDFK());
        Valor.put("LocalizacaoID",localizacao.getLocalizacaoID());
        Valor.put("Descricao",localizacao.getDescricao());
        Valor.put("Complemento",localizacao.getComplemento());
        Valor.put("Telefone",localizacao.getTelefone());
        Valor.put("LogUsuario",localizacao.getLogUsuario());

        try{

            Escreve.insert(ConfiguracaoSQLite.TABELA_LOCALIZACAO,null, Valor);

        }catch (Exception e ){
            return false;
        }

        return true;
    }

    @Override
    public boolean Atualizar(Localizacao localizacao) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFJ",localizacao.getClienteIDFK());
        Valor.put("Descricao",localizacao.getDescricao());
        Valor.put("Complemento",localizacao.getComplemento());
        Valor.put("Telefone",localizacao.getTelefone());
        Valor.put("LogUsuario",localizacao.getLogUsuario());

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf(localizacao.getLocalizacaoID())};

            Escreve.update(ConfiguracaoSQLite.TABELA_LOCALIZACAO, Valor ,"ClienteIDFK = ? AND LocalizacaoID = ?" , args);

        }catch (Exception e ){

            return false;
        }

        return true;
    }

    @Override
    public boolean Deletar(Localizacao localizacao) {

        try{
            String[] args = { String.valueOf(clienteIDFK), String.valueOf( localizacao.getLocalizacaoID() )};
            Escreve.delete(ConfiguracaoSQLite.TABELA_LOCALIZACAO,"ClienteIDFK = ? AND LocalizacaoID = ?" , args);

        }catch (Exception e ){

            return false;
        }

        return true;
    }

    @Override
    public List<Localizacao> Lista(Bem bem) {

        String Descricao, Complemento, Telefone, LogUsuario;
        int ClienteIDFK, LocalizacaoID;
        List<Localizacao> localizacaos = new ArrayList<>();

        String[] args = { String.valueOf( clienteIDFK )};

        String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_LOCALIZACAO + " WHERE ClienteIDFK = ? ORDER BY LocalizacaoID;";
        Cursor cursor = Ler.rawQuery(sql,args);

        while (cursor.moveToNext()){

            Localizacao localizacao = new Localizacao();

            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            LocalizacaoID = cursor.getInt(cursor.getColumnIndex("LocalizacaoID"));
            Descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
            Complemento = cursor.getString(cursor.getColumnIndex("Complemento"));
            Telefone = cursor.getString(cursor.getColumnIndex("Telefone"));
            LogUsuario = cursor.getString(cursor.getColumnIndex("LogUsuario"));

            localizacao.setClienteIDFK(ClienteIDFK);
            localizacao.setLocalizacaoID(LocalizacaoID);
            localizacao.setDescricao(Descricao);
            localizacao.setComplemento(Complemento);
            localizacao.setTelefone(Telefone);
            localizacao.setLogUsuario(LogUsuario);

            localizacaos.add(localizacao);
       }

        return localizacaos;
    }

    public List<Localizacao> Pesquisa(String localizacao) {

        String Descricao, Complemento, Telefone, LogUsuario;
        int ClienteIDFK, LocalizacaoID;
        List<Localizacao> localizacoes = new ArrayList<>();

        Cursor cursor;
        String sql;
        String[] args = { String.valueOf( clienteIDFK ), "%" + localizacao + "%"};

        localizacao = localizacao.toUpperCase();

        if (localizacao.length() > 0 ) {

            if (localizacao.matches(".*[A-Z].*")) {

                sql = " SELECT * FROM " + ConfiguracaoSQLite.TABELA_LOCALIZACAO + " WHERE ClienteIDFK = ? AND  Upper(Descricao) LIKE ? ;";

            } else {
                sql = " SELECT * FROM " + ConfiguracaoSQLite.TABELA_LOCALIZACAO + " WHERE ClienteIDFK = ? AND LocalizacaoID LIKE ? ;";
            }

            cursor = Ler.rawQuery(sql,args);

        }else  {

            args = new String[]{ String.valueOf( clienteIDFK )};

            sql = " SELECT * FROM " + ConfiguracaoSQLite.TABELA_LOCALIZACAO + " WHERE ClienteIDFK = ? ;";

            cursor = Ler.rawQuery(sql,args);
        }

        while (cursor.moveToNext()){

            Localizacao localizacao1 = new Localizacao();

            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            LocalizacaoID = cursor.getInt(cursor.getColumnIndex("LocalizacaoID"));
            Descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
            Complemento = cursor.getString(cursor.getColumnIndex("Complemento"));
            Telefone = cursor.getString(cursor.getColumnIndex("Telefone"));
            LogUsuario = cursor.getString(cursor.getColumnIndex("LogUsuario"));

            localizacao1.setClienteIDFK(ClienteIDFK);
            localizacao1.setLocalizacaoID(LocalizacaoID);
            localizacao1.setDescricao(Descricao);
            localizacao1.setComplemento(Complemento);
            localizacao1.setTelefone(Telefone);
            localizacao1.setLogUsuario(LogUsuario);

            localizacoes.add(localizacao1);
        }

        return localizacoes;
    }

}
