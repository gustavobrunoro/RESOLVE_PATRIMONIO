package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;

public class TipoTombo {

    int ID, TipoTomboID;
    String Descricao;

    public TipoTombo() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTipoTomboID() {
        return TipoTomboID;
    }

    public void setTipoTomboID(int tipoTomboID) {
        TipoTomboID = tipoTomboID;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

}

