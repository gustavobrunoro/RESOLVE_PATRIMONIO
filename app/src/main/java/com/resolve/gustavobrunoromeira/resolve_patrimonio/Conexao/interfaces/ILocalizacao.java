package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Localizacao;

import java.util.List;

public interface ILocalizacao {

    // Metodo Responsavel por Salvar as Informações
    public boolean Salvar(Localizacao localizacao);

    // Metodo Responsavel por Atualizar as Informações
    public boolean Atualizar(Localizacao localizacao);

    // Metodo Responsavel por Deleta as Informações
    public boolean Deletar(Localizacao localizacao);

    // Metodo Responsavel por Lista as Informações
    public List<Localizacao> Lista(Bem bem);
}
