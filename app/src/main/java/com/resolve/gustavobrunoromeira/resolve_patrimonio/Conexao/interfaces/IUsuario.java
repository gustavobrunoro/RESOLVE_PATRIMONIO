package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Usuario;

import java.util.List;

public interface IUsuario {

    // Metodo Responsavel por Salvar as Informações
    public boolean Salvar(Usuario usuario);

    // Metodo Responsavel por Atualizar as Informações
    public boolean Atualizar(Usuario usuario);

    // Metodo Responsavel por Deleta as Informações
    public boolean Deletar(Usuario usuario);

    // Metodo Responsavel por Lista as Informações
    public List<Usuario> Lista();


}
