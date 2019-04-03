package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces;


import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCustoLocalResponsavel;

import java.util.List;

public interface ICentroCustoLocalResponsavel {

    // Metodo Responsavel por Salvar as Informações
    public boolean Salvar(CentroCustoLocalResponsavel centroCustoLocalResponsavel);

    // Metodo Responsavel por Atualizar as Informações
    public boolean Atualizar(CentroCustoLocalResponsavel centroCustoLocalResponsavel);

    // Metodo Responsavel por Deleta as Informações
    public boolean Deletar(CentroCustoLocalResponsavel centroCustoLocalResponsavel);

    // Metodo Responsavel por Lista as Informações
    public List<CentroCustoLocalResponsavel> Lista(Bem bem);
}
