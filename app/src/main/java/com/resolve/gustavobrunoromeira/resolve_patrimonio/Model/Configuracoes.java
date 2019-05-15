package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;

public class Configuracoes {

    private int ClienteIDFK;
    private boolean Exporta;

    public Configuracoes() {
    }

    public int getClienteIDFK() {
        return ClienteIDFK;
    }

    public void setClienteIDFK(int clienteIDFK) {
        ClienteIDFK = clienteIDFK;
    }

    public boolean getExporta() {
        return Exporta;
    }

    public void setExporta(boolean exporta) {
        Exporta = exporta;
    }
}
