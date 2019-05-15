package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Interfaces;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Secretaria;

import java.util.List;

public interface ISecretaria {

    // Metodo Responsavel por Salvar as Informações
    public boolean Salvar(Secretaria secretaria);

    // Metodo Responsavel por Atualizar as Informações
    public boolean Atualizar(Secretaria secretaria);

    // Metodo Responsavel por Deleta as Informações
    public boolean Deletar(Secretaria secretaria);

    // Metodo Responsavel por Lista as Informações
    public List<Secretaria> Lista();

}
