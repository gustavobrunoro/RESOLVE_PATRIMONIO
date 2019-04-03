package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces.ICentroCustoLocalResponsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCustoLocalResponsavel;

import java.util.ArrayList;
import java.util.List;

public class CentroCustoLocalReponsavelDAO implements ICentroCustoLocalResponsavel {

    private SQLiteDatabase Escreve,Ler;
    private int clienteIDFK = 99;

    public CentroCustoLocalReponsavelDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    @Override
    public boolean Salvar(CentroCustoLocalResponsavel centroCustoLocalResponsavel) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",centroCustoLocalResponsavel.getClienteIDFK());
        Valor.put("CentroCustoIDFK",centroCustoLocalResponsavel.getCentroCustoIDFK());
        Valor.put("LocalizacaoIDFK",centroCustoLocalResponsavel.getLocalizacaoIDFK());
        Valor.put("Matricula",centroCustoLocalResponsavel.getMatricula());

        try{

            Escreve.insert(ConfiguracaoSQLite.TABELA_CENTROCUSTOLOCALRESPONSAVEL,null, Valor);

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    @Override
    public boolean Atualizar(CentroCustoLocalResponsavel centroCustoLocalResponsavel) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",centroCustoLocalResponsavel.getClienteIDFK());
        Valor.put("CentroCustoIDFK",centroCustoLocalResponsavel.getCentroCustoIDFK());
        Valor.put("LocalizacaoIDFK",centroCustoLocalResponsavel.getLocalizacaoIDFK());
        Valor.put("Matricula",centroCustoLocalResponsavel.getMatricula());

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf(centroCustoLocalResponsavel.getCentroCustoIDFK()), String.valueOf(centroCustoLocalResponsavel.getLocalizacaoIDFK()), String.valueOf(centroCustoLocalResponsavel.getMatricula())};

            Escreve.update(ConfiguracaoSQLite.TABELA_CENTROCUSTOLOCALRESPONSAVEL, Valor ,"ClienteIDFK = ? AND CentroCustoIDFK = ? AND LocalizacaoIDFK = ? AND Matricula = ?" , args);

        }catch (Exception e ){

            return false;
        }
        return true;
    }

    @Override
    public boolean Deletar(CentroCustoLocalResponsavel centroCustoLocalResponsavel) {

        try{
            String[] args = { String.valueOf( clienteIDFK ), String.valueOf(centroCustoLocalResponsavel.getCentroCustoIDFK()), String.valueOf(centroCustoLocalResponsavel.getLocalizacaoIDFK()), String.valueOf(centroCustoLocalResponsavel.getMatricula())};
             Escreve.delete(ConfiguracaoSQLite.TABELA_CENTROCUSTOLOCALRESPONSAVEL,"ClienteIDFK = ? AND CentroCustoIDFK = ? AND LocalizacaoIDFK = ? AND Matricula = ?" , args);


        }catch (Exception e ){

            return false;
        }

        return true;
    }

    @Override
    public List<CentroCustoLocalResponsavel> Lista(Bem bem) {

        int ClienteIDFK, CentroCustoIDFK, LocalizacaoIDFK, Matricula;
        List<CentroCustoLocalResponsavel> centroCustoLocalResponsavels = new ArrayList<>();

        Cursor cursor;

        String[] args = { String.valueOf( clienteIDFK ), String.valueOf( bem.getSecretariaIDFK() ), String.valueOf( bem.getLocalizacaoIDFK() )};

        String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_CENTROCUSTOLOCALRESPONSAVEL  + " WHERE ClienteIDFK = ? AND SecretariaIDFK = ? AND LocalizacaoIDFK = ? ";
        cursor = Ler.rawQuery(sql,args);

        while (cursor.moveToNext()){

            CentroCustoLocalResponsavel centroCustoLocalResponsavel = new CentroCustoLocalResponsavel();

            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            CentroCustoIDFK = cursor.getInt(cursor.getColumnIndex("CentroCustoIDFK"));
            LocalizacaoIDFK = cursor.getInt(cursor.getColumnIndex("SecretariaIDFK"));
            Matricula = cursor.getInt(cursor.getColumnIndex("Matricula"));

            centroCustoLocalResponsavel.setClienteIDFK(ClienteIDFK);
            centroCustoLocalResponsavel.setCentroCustoIDFK(CentroCustoIDFK);
            centroCustoLocalResponsavel.setLocalizacaoIDFK(LocalizacaoIDFK);
            centroCustoLocalResponsavel.setMatricula(Matricula);
        }

        return centroCustoLocalResponsavels;
    }

}
