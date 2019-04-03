package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Item;

import java.util.List;

public interface IItem {

    // Metodo Responsavel por Salvar as Informações
    public boolean Salvar(Item item);

    // Metodo Responsavel por Atualizar as Informações
    public boolean Atualizar(Item item);

    // Metodo Responsavel por Deleta as Informações
    public boolean Deletar(Item item);

    // Metodo Responsavel por Lista as Informações
    public List<Item> Lista(Bem bem);


}
