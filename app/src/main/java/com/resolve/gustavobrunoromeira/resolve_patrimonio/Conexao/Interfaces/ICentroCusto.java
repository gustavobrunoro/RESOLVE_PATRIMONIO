package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Interfaces;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCusto;

import java.util.List;

public interface ICentroCusto {

    // Metodo Responsavel por Salvar as Informações
    public boolean Salvar(CentroCusto centroCusto);

    // Metodo Responsavel por Atualizar as Informações
    public boolean Atualizar(CentroCusto centroCusto);

    // Metodo Responsavel por Deleta as Informações
    public boolean Deletar(CentroCusto centroCusto);

    // Metodo Responsavel por Lista as Informações
    public List<CentroCusto> Lista(Bem bem);

}
