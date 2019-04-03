package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;

public class Fabricante  {

    int ID, ClienteIDFK, FabricanteID;
    String Nome, Descricao;

    public Fabricante() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getClienteIDFK() {
        return ClienteIDFK;
    }

    public void setClienteIDFK(int clienteIDFK) {
        this.ClienteIDFK = clienteIDFK;
    }

    public int getFabricanteID() {
        return FabricanteID;
    }

    public void setFabricanteID(int fabricanteID) {
        FabricanteID = fabricanteID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

}
