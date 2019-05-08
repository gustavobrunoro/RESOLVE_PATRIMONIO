package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;

public class Localizacao {

    private int ID,ClienteIDFK, LocalizacaoID;
    private String Descricao, Endereco, Numero, Rua, Bairro, Cidade, Complemento, CEP, Telefone, LogUsuario;

    public Localizacao() {}

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

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getRua() {
        return Rua;
    }

    public void setRua(String rua) {
        Rua = rua;
    }

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        Bairro = bairro;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        Cidade = cidade;
    }

    public String getComplemento() {
        return Complemento;
    }

    public void setComplemento(String complemento) {
        Complemento = complemento;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
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

