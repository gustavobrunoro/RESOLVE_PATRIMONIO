package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Interfaces.IEstadoConservacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.EstadoConservacao;

import java.util.ArrayList;
import java.util.List;

public class EstadoConsevacaoDAO implements IEstadoConservacao {

    private SQLiteDatabase Escreve,Ler;
    private long retorno;

    public EstadoConsevacaoDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    /**Metodos Responsavel Por Salvar o Estado de Conservação no Banco de Dados
     @param estadoConservacao
     @return Boolean */
    @Override
    public boolean Salvar(EstadoConservacao estadoConservacao) {


        ContentValues Valor = new ContentValues();
        Valor.put("EstadoConservacaoID",estadoConservacao.getEstadoConservacaoID());
        Valor.put("Descricao",estadoConservacao.getDescricao());

        try{

            retorno = Escreve.insert(ConfiguracaoSQLite.TABELA_ESTADOCONSERVACAO,null, Valor);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Atualizar o Estado de Conservação no Banco de Dados
     @param estadoConservacao
     @return Boolean */
    @Override
    public boolean Atualizar(EstadoConservacao estadoConservacao) {

        ContentValues Valor = new ContentValues();
        Valor.put("Descricao",estadoConservacao.getDescricao());

        try{

            String[] args = { String.valueOf(estadoConservacao.getEstadoConservacaoID())};

            retorno =  Escreve.update(ConfiguracaoSQLite.TABELA_ESTADOCONSERVACAO, Valor ,"EstadoConservacaoID = ?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Deletar o Estado de Conservação no Banco de Dados
     @param estadoConservacao
     @return Boolean */
    @Override
    public boolean Deletar(EstadoConservacao estadoConservacao) {

        try{

            String[] args = { String.valueOf( estadoConservacao.getEstadoConservacaoID() )};
            retorno = Escreve.delete(ConfiguracaoSQLite.TABELA_ESTADOCONSERVACAO,"EstadoConservacaoID = ?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por LIsta o Estado de Conservação no Banco de Dados
     @return Lista do Estado de Conservação */
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
