package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Fabricante;

import java.util.List;

public interface IFabricante {

    // Metodo Fabricante por Salvar as Informações
    public boolean Salvar(Fabricante fabricante);

    // Metodo Fabricante por Atualizar as Informações
    public boolean Atualizar(Fabricante fabricante);

    // Metodo Fabricante por Deleta as Informações
    public boolean Deletar(Fabricante fabricante);

    // Metodo Fabricante por Lista as Informações
    public List<Fabricante> Lista(Bem bem);

}
