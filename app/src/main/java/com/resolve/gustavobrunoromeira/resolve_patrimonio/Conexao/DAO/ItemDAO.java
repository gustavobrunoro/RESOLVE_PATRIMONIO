package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces.IItem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDAO implements IItem {

    private SQLiteDatabase Escreve,Ler;
    private int clienteIDFK = 99;
    private long retorno;

    public ItemDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    /**Metodos Responsavel Por Salvar o Item no Banco de Dados
     @param item
     @return Boolean */
    @Override
    public boolean Salvar(Item item) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",item.getClienteIDFK());
        Valor.put("ItemID",item.getItemID());
        Valor.put("Descricao",item.getDescricao());

        try{

            retorno = Escreve.insert(ConfiguracaoSQLite.TABELA_ITEM,null, Valor);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Atualizar o Item no Banco de Dados
     @param item
     @return Boolean */
    @Override
    public boolean Atualizar(Item item) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFJ",item.getClienteIDFK());
        Valor.put("Descricao",item.getDescricao());

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf(item.getItemID())};

            retorno = Escreve.update(ConfiguracaoSQLite.TABELA_ITEM, Valor ,"ClienteIDFK = ? AND ItemID=?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Deletar o Item no Banco de Dados
     @param item
     @return Boolean */
    @Override
    public boolean Deletar(Item item) {

        try{
            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( item.getItemID())};
            retorno = Escreve.delete(ConfiguracaoSQLite.TABELA_ITEM,"ClienteIDFK = ? AND ItemID = ?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Lista o Item no Banco de Dados
     @param item
     @return Lista de Itens pesquisados */
    @Override
    public List<Item> Lista(Bem bem) {

        String Descricao;
        int ClienteIDFK, ItemID;
        List<Item> itens = new ArrayList<>();

        String[] args = { String.valueOf( clienteIDFK )};

        String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_ITEM + " WHERE ClienteIDFK = ? ORDER BY Descricao;";
        Cursor cursor = Ler.rawQuery(sql,args);

        while (cursor.moveToNext()){

            Item item = new Item();

            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            ItemID = cursor.getInt(cursor.getColumnIndex("ItemID"));
            Descricao = cursor.getString(cursor.getColumnIndex("Descricao"));

            item.setClienteIDFK(ClienteIDFK);
            item.setItemID(ItemID);
            item.setDescricao(Descricao);

            itens.add(item);
        }
        return itens;
    }

}
