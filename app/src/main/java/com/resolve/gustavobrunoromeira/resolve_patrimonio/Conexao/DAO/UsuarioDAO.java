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

    public UsuarioDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    @Override
    public boolean Salvar(Usuario usuario) {

        ContentValues Valor = new ContentValues();

        Valor.put("ClienteIDFK",usuario.getClienteIDFK());
        Valor.put("Nome",usuario.getNome());
        Valor.put("Email",usuario.getEmail());

        try{

            Escreve.insert(ConfiguracaoSQLite.TABELA_USUARIO ,null, Valor);

        }catch (Exception e ){
            return false;
        }

        return true;
    }

    @Override
    public boolean Atualizar(Usuario usuario) {

        ContentValues Valor = new ContentValues();

        Valor.put("ClienteIDFK",usuario.getClienteIDFK());
        Valor.put("Nome",usuario.getNome());
        Valor.put("Email",usuario.getEmail());

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( usuario.getEmail() )};

            Escreve.update(ConfiguracaoSQLite.TABELA_USUARIO, Valor ,"ClienteIDFK = ? AND Email=?" , args);

        }catch (Exception e ){

            return false;
        }

        return true;
    }

    @Override
    public boolean Deletar(Usuario usuario) {

        try{
            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( usuario.getEmail() )};
            Escreve.delete(ConfiguracaoSQLite.TABELA_USUARIO ,"ClienteIDFK = ? AND Email=?" , args);

        }catch (Exception e ){

            return false;
        }

        return true;
    }

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
