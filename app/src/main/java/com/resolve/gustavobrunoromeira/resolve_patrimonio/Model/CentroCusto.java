package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;


public class CentroCusto  {

    private int ID,ClienteIDFK,CentroCustoID,SecretariaIDFK,ResponsavelIDFK;
    private String Descricao;

    public CentroCusto() {
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

    public int getCentroCustoID() {
        return CentroCustoID;
    }

    public void setCentroCustoID(int centroCustoID) {
        CentroCustoID = centroCustoID;
    }

    public int getSecretariaIDFK() {
        return SecretariaIDFK;
    }

    public void setSecretariaIDFK(int secretariaIDFK) {
        SecretariaIDFK = secretariaIDFK;
    }

    public int getResponsavelIDFK() {
        return ResponsavelIDFK;
    }

    public void setResponsavelIDFK(int responsavelIDFK) {
        ResponsavelIDFK = responsavelIDFK;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

}
