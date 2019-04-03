package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;


public class EstadoConservacao {

    int ID, EstadoConservacaoID;
    String Descricao;

    public EstadoConservacao() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getEstadoConservacaoID() {
        return EstadoConservacaoID;
    }

    public void setEstadoConservacaoID(int estadoConservacaoID) {
        EstadoConservacaoID = estadoConservacaoID;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

}
