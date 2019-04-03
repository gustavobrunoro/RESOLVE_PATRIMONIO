package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;

public class CentroCustoLocalResponsavel {

    private int ID, ClienteIDFK, CentroCustoIDFK, LocalizacaoIDFK, Matricula ;

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

    public int getCentroCustoIDFK() {
        return CentroCustoIDFK;
    }

    public void setCentroCustoIDFK(int centroCustoIDFK) {
        CentroCustoIDFK = centroCustoIDFK;
    }

    public int getLocalizacaoIDFK() {
        return LocalizacaoIDFK;
    }

    public void setLocalizacaoIDFK(int localizacaoIDFK) {
        LocalizacaoIDFK = localizacaoIDFK;
    }

    public int getMatricula() {
        return Matricula;
    }

    public void setMatricula(int matricula) {
        Matricula = matricula;
    }
}
