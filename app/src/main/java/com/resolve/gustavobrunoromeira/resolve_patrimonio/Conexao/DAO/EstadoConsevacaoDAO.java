package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces.IEstadoConservacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.EstadoConservacao;

import java.util.ArrayList;
import java.util.List;

public class EstadoConsevacaoDAO implements IEstadoConservacao {

    private SQLiteDatabase Escreve,Ler;

    public EstadoConsevacaoDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    @Override
    public boolean Salvar(EstadoConservacao estadoConservacao) {


        ContentValues Valor = new ContentValues();
        Valor.put("EstadoConservacaoID",estadoConservacao.getEstadoConservacaoID());
        Valor.put("Descricao",estadoConservacao.getDescricao());

        try{

            Escreve.insert(ConfiguracaoSQLite.TABELA_ESTADOCONSERVACAO,null, Valor);

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    @Override
    public boolean Atualizar(EstadoConservacao estadoConservacao) {

        ContentValues Valor = new ContentValues();
        Valor.put("Descricao",estadoConservacao.getDescricao());

        try{

            String[] args = { String.valueOf(estadoConservacao.getEstadoConservacaoID())};

            Escreve.update(ConfiguracaoSQLite.TABELA_ESTADOCONSERVACAO, Valor ,"EstadoConservacaoID = ?" , args);

        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean Deletar(EstadoConservacao estadoConservacao) {

        try{
            String[] args = { String.valueOf( estadoConservacao.getEstadoConservacaoID() )};
            Escreve.delete(ConfiguracaoSQLite.TABELA_ESTADOCONSERVACAO,"EstadoConservacaoID = ?" , args);

        }catch (Exception e ){

            return false;
        }

        return true;

    }

    @Override
    public List<EstadoConservacao> Lista() {

        int EstadoConservacaoID;
        String Descricao;

        List<EstadoConservacao> estadoConservacaos = new ArrayList<>();

        String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_ESTADOCONSERVACAO + " ;";
        Cursor cursor = Ler.rawQuery(sql,null);

        while (cursor.moveToNext()){

            EstadoConservacao estadoConservacao = new EstadoConservacao();

            EstadoConservacaoID = cursor.getInt(cursor.getColumnIndex("EstadoConservacaoID"));
            Descricao = cursor.getString(cursor.getColumnIndex("Descricao"));

            estadoConservacao.setEstadoConservacaoID(EstadoConservacaoID);
            estadoConservacao.setDescricao(Descricao);

            estadoConservacaos.add(estadoConservacao);
        }

        return estadoConservacaos;
    }

}
