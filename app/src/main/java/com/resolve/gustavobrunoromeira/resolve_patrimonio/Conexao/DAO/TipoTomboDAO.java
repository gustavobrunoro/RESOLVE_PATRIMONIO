package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces.ITipoTombo;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.TipoTombo;

import java.util.ArrayList;
import java.util.List;

public class TipoTomboDAO implements ITipoTombo {

    private SQLiteDatabase Escreve,Ler;
    private long retorno;

    public TipoTomboDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    /**Metodos Responsavel Por Salvar o Tipo do Tombo no Banco de Dados
     @param tipoTombo
     @return Boolean */
    @Override
    public boolean Salvar(TipoTombo tipoTombo) {

        ContentValues Valor = new ContentValues();
        Valor.put("TipoTomboID",tipoTombo.getTipoTomboID());
        Valor.put("Descricao",tipoTombo.getDescricao());

        try{

            retorno = Escreve.insert(ConfiguracaoSQLite.TABELA_TIPOTOMBO,null, Valor);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Atualizar o Tipo do Tombo no Banco de Dados
     @param tipoTombo
     @return Boolean */
    @Override
    public boolean Atualizar(TipoTombo tipoTombo) {

        ContentValues Valor = new ContentValues();
        Valor.put("Descricao",tipoTombo.getDescricao());

        try{

            String[] args = {String.valueOf(tipoTombo.getTipoTomboID())};
            retorno = Escreve.update(ConfiguracaoSQLite.TABELA_TIPOTOMBO, Valor ,"TipoTomboID=?" , args);

            if ( retorno == - 1 )
                return false;

        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Deletar o Tipo do Tombo no Banco de Dados
     @param tipoTombo
     @return Boolean */
    @Override
    public boolean Deletar(TipoTombo tipoTombo) {

        try{
            String[] args = { String.valueOf( tipoTombo.getTipoTomboID() )};
            retorno = Escreve.delete(ConfiguracaoSQLite.TABELA_TIPOTOMBO,"TipoTomboID=?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Lista o Tipo do Tombo no Banco de Dados
      @return Lista de Tipos de Tombos */
    @Override
    public List<TipoTombo> Lista() {

        int TipoTomboID;
        String Descricao;

        List<TipoTombo> tipoTombos = new ArrayList<>();

        String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_TIPOTOMBO + " ORDER BY Descricao;";
        Cursor cursor = Ler.rawQuery(sql,null);

        while (cursor.moveToNext()){

            TipoTombo tipoTombo = new TipoTombo();

            TipoTomboID = cursor.getInt(cursor.getColumnIndex("TipoTomboID"));
            Descricao = cursor.getString(cursor.getColumnIndex("Descricao"));

            tipoTombo.setTipoTomboID(TipoTomboID);
            tipoTombo.setDescricao(Descricao);

            tipoTombos.add(tipoTombo);
        }
        return tipoTombos;
    }

}
