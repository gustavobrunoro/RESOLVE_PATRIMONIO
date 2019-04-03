package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;

public class Secretaria {

    private int ID,SecretariaID,ClienteIDFK,CodigoAnterior,SecretarioIDFK  ;
    private String Descricao, Endereco, Numero, Bairro, Telefone, NomeSecretario;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSecretariaID() {
        return SecretariaID;
    }

    public void setSecretariaID(int secretariaID) {
        SecretariaID = secretariaID;
    }

    public int getClienteIDFK() {
        return ClienteIDFK;
    }

    public void setClienteIDFK(int clienteIDFK) {
        ClienteIDFK = clienteIDFK;
    }

    public int getCodigoAnterior() {
        return CodigoAnterior;
    }

    public void setCodigoAnterior(int codigoAnterior) {
        CodigoAnterior = codigoAnterior;
    }

    public int getSecretarioIDFK() {
        return SecretarioIDFK;
    }

    public void setSecretarioIDFK(int secretarioIDFK) {
        SecretarioIDFK = secretarioIDFK;
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

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        Bairro = bairro;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getNomeSecretario() {
        return NomeSecretario;
    }

    public void setNomeSecretario(String nomeSecretario) {
        NomeSecretario = nomeSecretario;
    }

}

