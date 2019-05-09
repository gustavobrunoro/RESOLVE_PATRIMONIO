package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces.ICentroCusto;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCusto;

import java.util.ArrayList;
import java.util.List;

public class CentroCustoDAO implements ICentroCusto {

    private SQLiteDatabase Escreve,Ler;
    private int clienteIDFK = 99;
    private long retorno;

    public CentroCustoDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    /**Metodos Responsavel Por Salvar Centro Custo no Banco de Dados
     @param centroCusto
     @return Boolean */
    @Override
    public boolean Salvar(CentroCusto centroCusto) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",centroCusto.getClienteIDFK());
        Valor.put("CentroCustoID",centroCusto.getCentroCustoID());
        Valor.put("SecretariaIDFK",centroCusto.getSecretariaIDFK());
        Valor.put("Descricao",centroCusto.getDescricao());
        Valor.put("ResponsavelIDFK",centroCusto.getResponsavelIDFK());

        try{

           retorno = Escreve.insert(ConfiguracaoSQLite.TABELA_CENTROCUSTO,null, Valor);

           if ( retorno == -1 )
               return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Atualizar Centro Custo no Banco de Dados
     @param centroCusto
     @return Boolean */
    @Override
    public boolean Atualizar(CentroCusto centroCusto) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",centroCusto.getClienteIDFK());
        Valor.put("SecretariaIDFK",centroCusto.getSecretariaIDFK());
        Valor.put("Descricao",centroCusto.getDescricao());
        Valor.put("ResponsavelIDFK",centroCusto.getResponsavelIDFK());

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf(centroCusto.getCentroCustoID())};

           retorno = Escreve.update(ConfiguracaoSQLite.TABELA_CENTROCUSTO, Valor ,"ClienteIDFK = ? AND CentroCustoID = ?" , args);

           if ( retorno == -1 )
               return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Deletar Centro Custo no Banco de Dados
     @param centroCusto
     @return Boolean */
    @Override
    public boolean Deletar(CentroCusto centroCusto) {

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( centroCusto.getCentroCustoID() )};

            retorno = Escreve.delete(ConfiguracaoSQLite.TABELA_CENTROCUSTO,"ClienteIDFK = ? AND CentroCustoID = ?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodo Responsavel Por Lista Centro Custo no Banco de Dados
     @param bem
     @return Boolean */
    @Override
    public List<CentroCusto> Lista(Bem bem) {

        String Descricao;
        int ClienteIDFK,CentroCustoID,SecretariaIDFK,ResponsavelIDFK;
        List<CentroCusto> centroCustos = new ArrayList<>();

        Cursor cursor;

        if(bem.getSecretariaIDFK() != null){
            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( bem.getSecretariaIDFK() )};

            String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_CENTROCUSTO  + " WHERE ClienteIDFK = ? AND SecretariaIDFK= ?  ORDER BY CentroCustoID;";
            cursor = Ler.rawQuery(sql,args);

        }else  {
            String[] args = { String.valueOf( clienteIDFK )};

            String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_CENTROCUSTO + " WHERE ClienteIDFK = ? ORDER BY Descricao ;";
            cursor = Ler.rawQuery(sql,args);
        }

        while (cursor.moveToNext()){

            CentroCusto centroCusto = new CentroCusto();

            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            CentroCustoID = cursor.getInt(cursor.getColumnIndex("CentroCustoID"));
            SecretariaIDFK = cursor.getInt(cursor.getColumnIndex("SecretariaIDFK"));
            Descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
            ResponsavelIDFK = cursor.getInt(cursor.getColumnIndex("ResponsavelIDFK"));

            centroCusto.setClienteIDFK(ClienteIDFK);
            centroCusto.setCentroCustoID(CentroCustoID);
            centroCusto.setSecretariaIDFK(SecretariaIDFK);
            centroCusto.setDescricao(Descricao);
            centroCusto.setResponsavelIDFK(ResponsavelIDFK);

            centroCustos.add(centroCusto);
        }

        return centroCustos;
    }

    /**Metodos Responsavel Pesquisa Centro Custo
     @param CentroCusto
     @return Centro Custos*/
    public List<CentroCusto> Pesquisa (String CentroCusto) {

        String Descricao;
        int ClienteIDFK,CentroCustoID,SecretariaIDFK,ResponsavelIDFK;
        List<CentroCusto> centroCustos = new ArrayList<>();

        Cursor cursor;
        String sql;
        String[] args = { String.valueOf( clienteIDFK ), "%" + CentroCusto + "%"};

        CentroCusto = CentroCusto.toUpperCase();

        if (CentroCusto.length() > 0 ) {

            if (CentroCusto.matches(".*[A-Z].*")) {

                sql = " SELECT * FROM " + ConfiguracaoSQLite.TABELA_CENTROCUSTO + " WHERE ClienteIDFK = ? AND Upper(Descricao) LIKE ? ;";

            } else {
                sql = " SELECT * FROM " + ConfiguracaoSQLite.TABELA_CENTROCUSTO + " WHERE ClienteIDFK = ? AND CentroCustoID LIKE ? ;";
            }

            cursor = Ler.rawQuery(sql,args);

        }else  {
            args = new String[]{ String.valueOf( clienteIDFK )};

            sql = " SELECT *  FROM " + ConfiguracaoSQLite.TABELA_CENTROCUSTO + " WHERE ClienteIDFK = ?;";

            cursor = Ler.rawQuery(sql,args);
        }

        while (cursor.moveToNext()) {

            CentroCusto centroCusto1 = new CentroCusto();

            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            CentroCustoID = cursor.getInt(cursor.getColumnIndex("CentroCustoID"));
            SecretariaIDFK = cursor.getInt(cursor.getColumnIndex("SecretariaIDFK"));
            Descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
            ResponsavelIDFK = cursor.getInt(cursor.getColumnIndex("ResponsavelIDFK"));

            centroCusto1.setClienteIDFK(ClienteIDFK);
            centroCusto1.setCentroCustoID(CentroCustoID);
            centroCusto1.setSecretariaIDFK(SecretariaIDFK);
            centroCusto1.setDescricao(Descricao);
            centroCusto1.setResponsavelIDFK(ResponsavelIDFK);

            centroCustos.add(centroCusto1);
        }
        return centroCustos;
    }

    /**Metodos Responsavel Pesquisa Centro Custo
     @param ResponsavelIDFK
     @return Centro Custo*/
    public String PesquisaReponsavel (String ResponsavelIDFK) {

        String Reposnavel = "";
        Cursor cursor;

        String[] args = { ResponsavelIDFK };

        String sql;

        sql = " SELECT r.Nome "
            + " FROM " + ConfiguracaoSQLite.TABELA_CENTROCUSTO + " cc "
            + "      INNER JOIN " + ConfiguracaoSQLite.TABELA_RESPONSAVEL + " r ON cc.ResponsavelIDFK = r.Matricula "
            + " WHERE r.Matricula = ? ";

        cursor = Ler.rawQuery(sql,args);

        while (cursor.moveToNext()) {
            Reposnavel = cursor.getString(cursor.getColumnIndex("Nome"));
        }
        return Reposnavel;
    }

}
