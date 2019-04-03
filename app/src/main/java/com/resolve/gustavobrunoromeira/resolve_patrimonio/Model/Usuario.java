package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.UsuarioDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoFirebase;

public class Usuario {

    private String IdUsuario, Nome, Email, Senha, ClienteIDFK;

    public Usuario() {

    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Exclude
    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    @Exclude
    public String getClienteIDFK() {
        return ClienteIDFK;
    }

    public void setClienteIDFK(String clienteIDFK) {
        ClienteIDFK = clienteIDFK;
    }

    public void SalvarUsuarioWeb(String idUsuario){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();

        databaseReference.child("Usuarios").child(this.IdUsuario).child("Dados Pessoais").setValue(this);

    }

    public void SalvarUsuarioLocal(Context context){

        UsuarioDAO usuarioDAO = new UsuarioDAO(context);

        if (usuarioDAO.Salvar(this)){

            Log.i("Info_Cadastro","Cadastro Atualizado Nome " + getNome() + " Email:" + getEmail());

        }else  {

            usuarioDAO.Atualizar(this);
            Log.i("Info_Cadastro","Cadastro Salvo Nome " + getNome() + " Email:" + getEmail());
        }

    }

}

