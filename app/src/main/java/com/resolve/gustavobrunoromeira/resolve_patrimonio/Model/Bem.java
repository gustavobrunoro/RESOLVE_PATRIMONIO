package com.resolve.gustavobrunoromeira.resolve_patrimonio.Model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Bem implements Serializable {


    @Expose
    private Integer BemID, ClienteIDFK, SecretariaIDFK, CentroCustoIDFK, LocalizacaoIDFK, ResponsavelIDFK,  ItemIDFK, FabricanteIDFK, EstadoConservacaoIDFK, TipoTomboIDFK, Exportado;

    @Expose
    private String  Plaqueta, Especificacao, Observacao;

    @Expose
    private String Valor;

    @Expose
    private String Foto1, Foto2;

    public Bem() {}

    public Integer getBemID() {
        return BemID;
    }

    public void setBemID(Integer bemID) {
        BemID = bemID;
    }

    public Integer getClienteIDFK() {
        return ClienteIDFK;
    }

    public void setClienteIDFK(Integer clienteIDFK) {
        ClienteIDFK = clienteIDFK;
    }

    public Integer getSecretariaIDFK() {
        return SecretariaIDFK;
    }

    public void setSecretariaIDFK(Integer secretariaIDFK) {
        SecretariaIDFK = secretariaIDFK;
    }

    public Integer getCentroCustoIDFK() {
        return CentroCustoIDFK;
    }

    public void setCentroCustoIDFK(Integer centroCustoIDFK) {
        CentroCustoIDFK = centroCustoIDFK;
    }

    public Integer getLocalizacaoIDFK() {
        return LocalizacaoIDFK;
    }

    public void setLocalizacaoIDFK(Integer localizacaoIDFK) {
        LocalizacaoIDFK = localizacaoIDFK;
    }

    public Integer getResponsavelIDFK() {
        return ResponsavelIDFK;
    }

    public void setResponsavelIDFK(Integer responsavelIDFK) {
        ResponsavelIDFK = responsavelIDFK;
    }

    public Integer getItemIDFK() {
        return ItemIDFK;
    }

    public void setItemIDFK(Integer itemIDFK) {
        ItemIDFK = itemIDFK;
    }

    public Integer getFabricanteIDFK() {
        return FabricanteIDFK;
    }

    public void setFabricanteIDFK(Integer fabricanteIDFK) {
        FabricanteIDFK = fabricanteIDFK;
    }

    public Integer getEstadoConservacaoIDFK() {
        return EstadoConservacaoIDFK;
    }

    public void setEstadoConservacaoIDFK(Integer estadoConservacaoIDFK) {
        EstadoConservacaoIDFK = estadoConservacaoIDFK;
    }

    public Integer getTipoTomboIDFK() {
        return TipoTomboIDFK;
    }

    public void setTipoTomboIDFK(Integer tipoTomboIDFK) {
        TipoTomboIDFK = tipoTomboIDFK;
    }

    public Integer getExportado() {
        return Exportado;
    }

    public void setExportado(Integer exportado) {
        Exportado = exportado;
    }

    public String getPlaqueta() {
        return Plaqueta;
    }

    public void setPlaqueta(String plaqueta) {
        Plaqueta = plaqueta;
    }

    public String getEspecificacao() {
        return Especificacao;
    }

    public void setEspecificacao(String especificacao) {
        Especificacao = especificacao;
    }

    public String getObservacao() {
        return Observacao;
    }

    public void setObservacao(String observacao) {
        Observacao = observacao;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getFoto1() {
        return Foto1;
    }

    public void setFoto1(String foto1) {
        Foto1 = foto1;
    }

    public String getFoto2() {
        return Foto2;
    }

    public void setFoto2(String foto2) {
        Foto2 = foto2;
    }

}
