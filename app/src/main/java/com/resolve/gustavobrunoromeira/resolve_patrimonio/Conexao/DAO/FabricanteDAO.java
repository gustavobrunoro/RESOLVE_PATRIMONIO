package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Interfaces.IFabricante;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Fabricante;

import java.util.ArrayList;
import java.util.List;

public class FabricanteDAO implements IFabricante {

    private SQLiteDatabase Escreve,Ler;
    private int clienteIDFK = 99;
    private long retorno;

    public FabricanteDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    /**Metodos Responsavel Por Salvar o Fabricante no Banco de Dados
     @param fabricante
     @return Boolean */
    @Override
    public boolean Salvar(Fabricante fabricante) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",fabricante.getClienteIDFK());
        Valor.put("FabricanteID",fabricante.getFabricanteID());
        Valor.put("Nome",fabricante.getNome());
        Valor.put("Descricao",fabricante.getDescricao());

        try{

            retorno = Escreve.insert(ConfiguracaoSQLite.TABELA_FABRICANTE,null, Valor);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Atualizar o Fabricante no Banco de Dados
     @param fabricante
     @return Boolean */
    @Override
    public boolean Atualizar(Fabricante fabricante) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",fabricante.getClienteIDFK());
        Valor.put("Nome",fabricante.getNome());
        Valor.put("Descricao",fabricante.getDescricao());

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf(fabricante.getFabricanteID())};

            retorno = Escreve.update(ConfiguracaoSQLite.TABELA_FABRICANTE, Valor ,"ClienteIDFK = ? AND FabricanteID = ?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Deletar o Fabricante no Banco de Dados
     @param fabricante
     @return Boolean */
    @Override
    public boolean Deletar(Fabricante fabricante) {

        try{
            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( fabricante.getFabricanteID())};
            retorno = Escreve.delete(ConfiguracaoSQLite.TABELA_FABRICANTE,"ClienteIDFK = ? AND FabricanteID = ?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Lista o Fabricante no Banco de Dados
     @param bem
     @return Lista de Fabricantes pesquisados */
    @Override
    public List<Fabricante> Lista(Bem bem) {

        String Descricao, Nome;
        int ClienteIDFK, FabricanteID;
        List<Fabricante> fabricantes = new ArrayList<>();

        String[] args = {String.valueOf( clienteIDFK )};

        String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_FABRICANTE + " WHERE ClienteIDFK = ? ORDER BY Descricao;";
        Cursor cursor = Ler.rawQuery(sql,args);

        while (cursor.moveToNext()){

            Fabricante fabricante = new Fabricante();

            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            FabricanteID = cursor.getInt(cursor.getColumnIndex("FabricanteID"));
            Nome = cursor.getString(cursor.getColumnIndex("Nome"));
            Descricao = cursor.getString(cursor.getColumnIndex("Descricao"));

            fabricante.setClienteIDFK(ClienteIDFK);
            fabricante.setFabricanteID(FabricanteID);
            fabricante.setNome(Nome);
            fabricante.setDescricao(Descricao);

            fabricantes.add(fabricante);
        }
        return fabricantes;
    }

}
