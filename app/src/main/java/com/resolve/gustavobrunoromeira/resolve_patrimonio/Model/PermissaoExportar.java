package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;

public class PermissaoExportar {

    private int ClienteIDFK, TipoPermissaoIDFK, Status;

    public PermissaoExportar() {
    }

    public int getClienteIDFK() {
        return ClienteIDFK;
    }

    public void setClienteIDFK(int clienteIDFK) {
        ClienteIDFK = clienteIDFK;
    }

    public int getTipoPermissaoIDFK() {
        return TipoPermissaoIDFK;
    }

    public void setTipoPermissaoIDFK(int tipoPermissaoIDFK) {
        TipoPermissaoIDFK = tipoPermissaoIDFK;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

}

