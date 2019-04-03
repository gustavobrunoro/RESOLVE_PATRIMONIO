package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;

import java.util.List;

public interface IBem {

    // Metodo Responsavel por Salvar as Informações
    public boolean Salvar(Bem item);

    // Metodo Responsavel por Atualizar as Informações
    public boolean Atualizar(Bem item);

    // Metodo Responsavel por Deleta as Informações
    public boolean Deletar(Bem item);

    // Metodo Responsavel por Lista as Informações
    public List<Bem> Lista(int exportado);
}
