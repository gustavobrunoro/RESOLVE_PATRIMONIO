package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.EstadoConservacao;

import java.util.List;

public interface IEstadoConservacao {

    // Metodo Responsavel por Salvar as Informações
    public boolean Salvar(EstadoConservacao estadoConservacao);

    // Metodo Responsavel por Atualizar as Informações
    public boolean Atualizar(EstadoConservacao estadoConservacao);

    // Metodo Responsavel por Deleta as Informações
    public boolean Deletar(EstadoConservacao estadoConservacao);

    // Metodo Responsavel por Lista as Informações
    public List<EstadoConservacao> Lista();

}
