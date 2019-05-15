package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Interfaces;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.TipoTombo;

import java.util.List;

public interface ITipoTombo {

    // Metodo Responsavel por Salvar as Informações
    public boolean Salvar(TipoTombo tipoTombo);

    // Metodo Responsavel por Atualizar as Informações
    public boolean Atualizar(TipoTombo tipoTombo);

    // Metodo Responsavel por Deleta as Informações
    public boolean Deletar(TipoTombo tipoTombo);

    // Metodo Responsavel por Lista as Informações
    public List<TipoTombo> Lista();

}
