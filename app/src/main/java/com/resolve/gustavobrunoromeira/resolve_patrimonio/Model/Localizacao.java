package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;

public class Localizacao {

    private int ID,ClienteIDFK, LocalizacaoID;
    private String Descricao, Complemento, Telefone, LogUsuario;

    public Localizacao() {
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
        ClienteIDFK = clienteIDFK;
    }

    public int getLocalizacaoID() {
        return LocalizacaoID;
    }

    public void setLocalizacaoID(int localizacaoID) {
        LocalizacaoID = localizacaoID;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getComplemento() {
        return Complemento;
    }

    public void setComplemento(String complemento) {
        Complemento = complemento;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getLogUsuario() {
        return LogUsuario;
    }

    public void setLogUsuario(String logUsuario) {
        LogUsuario = logUsuario;
    }

}

