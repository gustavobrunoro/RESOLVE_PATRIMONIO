package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Interfaces.ISecretaria;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Secretaria;

import java.util.ArrayList;
import java.util.List;

public class SecretariaDAO implements ISecretaria {

    private SQLiteDatabase Escreve,Ler;
    private int clienteIDFK = 99;
    private long retorno;

    public SecretariaDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    /**Metodos Responsavel Por Salvar Secretaria no Banco de Dados
     @param secretaria
     @return Boolean */
    @Override
    public boolean Salvar(Secretaria secretaria) {

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",secretaria.getClienteIDFK());
        Valor.put("SecretariaID",secretaria.getSecretariaID());
        Valor.put("Descricao",secretaria.getDescricao());
        Valor.put("SecretarioIDFK",secretaria.getSecretarioIDFK());
        Valor.put("NomeSecretario",secretaria.getNomeSecretario());
        Valor.put("Endereco",secretaria.getEndereco());
        Valor.put("Numero",secretaria.getNumero());
        Valor.put("Bairro",secretaria.getBairro());
        Valor.put("Telefone",secretaria.getTelefone());
        Valor.put("CodigoAnterior",secretaria.getCodigoAnterior());

        try{

            retorno = Escreve.insert(ConfiguracaoSQLite.TABELA_SECRETARIA,null, Valor);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Atualizar Secretaria no Banco de Dados
     @param secretaria
     @return Boolean */
    @Override
    public boolean Atualizar(Secretaria secretaria) {

        ContentValues Valor = new ContentValues();
        Valor.put("Descricao",secretaria.getDescricao());
        Valor.put("SecretarioIDFK",secretaria.getSecretarioIDFK());
        Valor.put("NomeSecretario",secretaria.getNomeSecretario());
        Valor.put("Endereco",secretaria.getEndereco());
        Valor.put("Numero",secretaria.getNumero());
        Valor.put("Bairro",secretaria.getBairro());
        Valor.put("Telefone",secretaria.getTelefone());

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf(secretaria.getSecretariaID())};
            retorno = Escreve.update(ConfiguracaoSQLite.TABELA_SECRETARIA, Valor ,"ClienteIDFK = ? AND SecretariaID = ?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Deletar Secretaria no Banco de Dados
     @param secretaria
     @return Boolean */
    @Override
    public boolean Deletar(Secretaria secretaria) {

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( secretaria.getID() )};
            retorno = Escreve.delete(ConfiguracaoSQLite.TABELA_SECRETARIA,"ClienteIDFK = ? AND SecretariaID = ?" , args);

            if( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Lista Secretaria
     @return Lista Secretaria*/
    @Override
    public List<Secretaria> Lista() {

        int ID, SecretariaID, ClienteIDFK, CodigoAnterior, SecretarioIDFK;
        String Descricao, Endereco, Numero, Bairro, Telefone, NomeSecretario;

        List<Secretaria> secretarias = new ArrayList<>();

        String[] args = new String[] { String.valueOf( clienteIDFK )};

        String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_SECRETARIA + " WHERE ClienteIDFK = ? ORDER BY SecretariaID ";
        Cursor cursor = Ler.rawQuery(sql,args);

        while (cursor.moveToNext()){

            Secretaria secretaria = new Secretaria();

            ID = cursor.getInt(cursor.getColumnIndex("ID"));
            SecretariaID = cursor.getInt(cursor.getColumnIndex("SecretariaID"));
            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            Descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
            SecretarioIDFK = cursor.getInt(cursor.getColumnIndex("SecretarioIDFK"));
            NomeSecretario = cursor.getString(cursor.getColumnIndex("NomeSecretario"));
            Endereco = cursor.getString(cursor.getColumnIndex("Endereco"));
            Numero = cursor.getString(cursor.getColumnIndex("Numero"));
            Bairro = cursor.getString(cursor.getColumnIndex("Bairro"));
            Telefone = cursor.getString(cursor.getColumnIndex("Telefone"));
            CodigoAnterior = cursor.getInt(cursor.getColumnIndex("CodigoAnterior"));

            secretaria.setID(ID);
            secretaria.setSecretariaID(SecretariaID);
            secretaria.setClienteIDFK(ClienteIDFK);
            secretaria.setDescricao(Descricao);
            secretaria.setSecretarioIDFK(SecretarioIDFK);
            secretaria.setNomeSecretario(NomeSecretario);
            secretaria.setEndereco(Endereco);
            secretaria.setNumero(Numero);
            secretaria.setBairro(Bairro);
            secretaria.setTelefone(Telefone);
            secretaria.setCodigoAnterior(CodigoAnterior);

            secretarias.add(secretaria);
        }
        return secretarias;
    }

    /**Metodos Responsavel Pesquisa Secretarias
     @param secretaria
     @return Lista de secretarias pesquisada*/
    public List<Secretaria> Pesquisa (String secretaria) {

        int ID, SecretariaID, ClienteIDFK, CodigoAnterior, SecretarioIDFK;
        String Descricao, Endereco, Numero, Bairro, Telefone, NomeSecretario;

        List<Secretaria> secretarias = new ArrayList<>();

        Cursor cursor;

        String[] args = { String.valueOf( clienteIDFK ), "%" + secretaria + "%"};

        String sql;

        secretaria = secretaria.toUpperCase();

        if (secretaria.length() > 0 ) {

            if (secretaria.matches(".*[A-Z].*")) {

                sql = " SELECT * FROM " + ConfiguracaoSQLite.TABELA_SECRETARIA + " WHERE ClienteIDFK = ? AND Upper(Descricao) LIKE ? ;";

            } else {
                sql = " SELECT * FROM " + ConfiguracaoSQLite.TABELA_SECRETARIA + " WHERE ClienteIDFK = ? AND SecretariaID LIKE ? ;";
            }

            cursor = Ler.rawQuery(sql,args);

        }else  {

            args = new String[] { String.valueOf( clienteIDFK )};

            sql = " SELECT *  FROM " + ConfiguracaoSQLite.TABELA_SECRETARIA + " WHERE ClienteIDFK = ? ;";

            cursor = Ler.rawQuery(sql,args);
        }

        while (cursor.moveToNext()) {

            Secretaria secretaria1 = new Secretaria();

            ID = cursor.getInt(cursor.getColumnIndex("ID"));
            SecretariaID = cursor.getInt(cursor.getColumnIndex("SecretariaID"));
            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            Descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
            SecretarioIDFK = cursor.getInt(cursor.getColumnIndex("SecretarioIDFK"));
            NomeSecretario = cursor.getString(cursor.getColumnIndex("NomeSecretario"));
            Endereco = cursor.getString(cursor.getColumnIndex("Endereco"));
            Numero = cursor.getString(cursor.getColumnIndex("Numero"));
            Bairro = cursor.getString(cursor.getColumnIndex("Bairro"));
            Telefone = cursor.getString(cursor.getColumnIndex("Telefone"));
            CodigoAnterior = cursor.getInt(cursor.getColumnIndex("CodigoAnterior"));

            secretaria1.setID(ID);
            secretaria1.setSecretariaID(SecretariaID);
            secretaria1.setClienteIDFK(ClienteIDFK);
            secretaria1.setDescricao(Descricao);
            secretaria1.setSecretarioIDFK(SecretarioIDFK);
            secretaria1.setNomeSecretario(NomeSecretario);
            secretaria1.setEndereco(Endereco);
            secretaria1.setNumero(Numero);
            secretaria1.setBairro(Bairro);
            secretaria1.setTelefone(Telefone);
            secretaria1.setCodigoAnterior(CodigoAnterior);

            secretarias.add(secretaria1);
        }
        return secretarias;
    }

}
