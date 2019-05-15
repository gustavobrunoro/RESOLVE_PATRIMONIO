package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Interfaces;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Responsavel;

import java.util.List;

public interface IResponsavel {

    // Metodo Responsavel por Salvar as Informações
    public boolean Salvar(Responsavel responsavel);

    // Metodo Responsavel por Atualizar as Informações
    public boolean Atualizar(Responsavel responsavel);

    // Metodo Responsavel por Deleta as Informações
    public boolean Deletar(Responsavel responsavel);

    // Metodo Responsavel por Lista as Informações
    public List<Responsavel> Lista(Bem bem);
}
