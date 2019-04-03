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

    public TipoTomboDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    @Override
    public boolean Salvar(TipoTombo tipoTombo) {

        ContentValues Valor = new ContentValues();
        Valor.put("TipoTomboID",tipoTombo.getTipoTomboID());
        Valor.put("Descricao",tipoTombo.getDescricao());

        try{

            Escreve.insert(ConfiguracaoSQLite.TABELA_TIPOTOMBO,null, Valor);

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    @Override
    public boolean Atualizar(TipoTombo tipoTombo) {

        ContentValues Valor = new ContentValues();
        Valor.put("Descricao",tipoTombo.getDescricao());

        try{

            String[] args = {String.valueOf(tipoTombo.getTipoTomboID())};

            Escreve.update(ConfiguracaoSQLite.TABELA_TIPOTOMBO, Valor ,"TipoTomboID=?" , args);

        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean Deletar(TipoTombo tipoTombo) {

        try{
            String[] args = { String.valueOf( tipoTombo.getTipoTomboID() )};
            Escreve.delete(ConfiguracaoSQLite.TABELA_TIPOTOMBO,"TipoTomboID=?" , args);

        }catch (Exception e ){

            return false;
        }

        return true;
    }

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
