package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces.IUsuario;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements IUsuario {

    private SQLiteDatabase Escreve,Ler;
    private int clienteIDFK = 99;
    private long retorno;

    public UsuarioDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    /**Metodos Responsavel Por Salvar o Usuario no Banco de Dados
     @param usuario
     @return Boolean */
    @Override
    public boolean Salvar(Usuario usuario) {

        ContentValues Valor = new ContentValues();

        Valor.put("ClienteIDFK",usuario.getClienteIDFK());
        Valor.put("Nome",usuario.getNome());
        Valor.put("Email",usuario.getEmail());

        try{

            retorno = Escreve.insert(ConfiguracaoSQLite.TABELA_USUARIO ,null, Valor);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Atualizar o Usuario no Banco de Dados
     @param usuario
     @return Boolean */
    @Override
    public boolean Atualizar(Usuario usuario) {

        ContentValues Valor = new ContentValues();

        Valor.put("ClienteIDFK",usuario.getClienteIDFK());
        Valor.put("Nome",usuario.getNome());
        Valor.put("Email",usuario.getEmail());

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( usuario.getEmail() )};
            retorno = Escreve.update(ConfiguracaoSQLite.TABELA_USUARIO, Valor ,"ClienteIDFK = ? AND Email=?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Deletar o Usuario no Banco de Dados
     @param usuario
     @return Boolean */
    @Override
    public boolean Deletar(Usuario usuario) {

        try{
            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( usuario.getEmail() )};
            retorno = Escreve.delete(ConfiguracaoSQLite.TABELA_USUARIO ,"ClienteIDFK = ? AND Email=?" , args);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Lista o Usuario no Banco de Dados
     @return Lista de usuarios */
    @Override
    public List<Usuario> Lista() {

        String Nome, Email;
        List<Usuario> usuarios = new ArrayList<>();

        String[] args = { String.valueOf( clienteIDFK )};

        String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_USUARIO;
        Cursor cursor = Ler.rawQuery(sql,null);

        while (cursor.moveToNext()){

            Usuario usuario = new Usuario();

            Nome = cursor.getString(cursor.getColumnIndex("Nome"));
            Email = cursor.getString(cursor.getColumnIndex("Email"));

            usuario.setNome(Nome);
            usuario.setEmail(Email);

            usuarios.add(usuario);

        }
        return usuarios;
    }

}
