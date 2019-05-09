package com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSQLite;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.interfaces.IBem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;

import java.util.ArrayList;
import java.util.List;

public class BemDAO implements IBem {

    private SQLiteDatabase Escreve,Ler;
    private int clienteIDFK = 99;
    private long retorno;

    public BemDAO(Context context) {

        ConfiguracaoSQLite configuracaoSQLite = new ConfiguracaoSQLite(context);

        Escreve = configuracaoSQLite.getWritableDatabase();
        Ler     = configuracaoSQLite.getReadableDatabase();
    }

    /**Metodos Responsavel Por Salvar Plaqueta
     @param bem
     @return Boolean*/
    @Override
    public boolean Salvar(Bem bem) {

        ContentValues Valor = new ContentValues();

        Valor.put("ClienteIDFK",bem.getClienteIDFK());
        Valor.put("Plaqueta",bem.getPlaqueta());
        Valor.put("SecretariaIDFK",bem.getSecretariaIDFK());
        Valor.put("CentroCustoIDFK",bem.getCentroCustoIDFK());
        Valor.put("LocalizacaoIDFK",bem.getLocalizacaoIDFK());
        Valor.put("ResponsavelIDFK",bem.getResponsavelIDFK());
        Valor.put("ItemIDFK",bem.getItemIDFK());
        Valor.put("Especificacao",bem.getEspecificacao());
        Valor.put("FabricanteIDFK",bem.getFabricanteIDFK());
        Valor.put("Valor", bem.getValor());
        Valor.put("EstadoConservacaoIDFK",bem.getEstadoConservacaoIDFK());
        Valor.put("TipoTomboIDFK",bem.getTipoTomboIDFK());
        Valor.put("Observacao",bem.getObservacao());
        Valor.put("Foto1",bem.getFoto1());
        Valor.put("Foto2",bem.getFoto2());
        Valor.put("Exportado",bem.getExportado());

        try{

            retorno = Escreve.insert(ConfiguracaoSQLite.TABELA_BEM,null, Valor);

            if ( retorno == -1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Atualizar Plaqueta
     @param bem
     @return Boolean*/
    @Override
    public boolean Atualizar(Bem bem) {

        List<Bem> bens = new ArrayList<>();

        bens = Lista(0);

        ContentValues Valor = new ContentValues();
        Valor.put("ClienteIDFK",bem.getClienteIDFK());
        Valor.put("SecretariaIDFK",bem.getSecretariaIDFK());
        Valor.put("CentroCustoIDFK",bem.getCentroCustoIDFK());
        Valor.put("LocalizacaoIDFK",bem.getLocalizacaoIDFK());
        Valor.put("ResponsavelIDFK",bem.getResponsavelIDFK());
        Valor.put("ItemIDFK",bem.getItemIDFK());
        Valor.put("Especificacao",bem.getEspecificacao());
        Valor.put("FabricanteIDFK",bem.getFabricanteIDFK());
        Valor.put("Valor",bem.getValor());
        Valor.put("EstadoConservacaoIDFK",bem.getEstadoConservacaoIDFK());
        Valor.put("TipoTomboIDFK",bem.getTipoTomboIDFK());
        Valor.put("Observacao",bem.getObservacao());
        Valor.put("Foto1",bem.getFoto1());
        Valor.put("Foto2",bem.getFoto2());
        Valor.put("Exportado",bem.getExportado());

        for (Bem b : bens){

            if (b.getPlaqueta().equals(bem.getPlaqueta())){

                try{

                    String[] args = { String.valueOf( clienteIDFK ), String.valueOf( bem.getPlaqueta() )};

                    retorno = Escreve.update(ConfiguracaoSQLite.TABELA_BEM, Valor ," ClienteIDFK = ? AND  Plaqueta = ?" , args);

                    if ( retorno == -1 )
                        return false;
                    else
                        return true;

                }catch (Exception e ){
                    return false;
                }
            }
        }
        return false;
    }

    /**Metodos Responsavel Por Deletar Plaqueta
     @param bem
     @return Boolean*/
    @Override
    public boolean Deletar(Bem bem) {

        try{

            String[] args = { String.valueOf( clienteIDFK ), String.valueOf( bem.getPlaqueta() )};
            retorno = Escreve.delete(ConfiguracaoSQLite.TABELA_BEM,"ClienteIDFK = ? AND Plaqueta = ?" , args);

            if ( retorno == - 1 )
                return false;

        }catch (Exception e ){
            return false;
        }
        return true;
    }

    /**Metodos Responsavel Por Lista Plaqueta
     @param exportado
     @return Lista Bem Cadastrados no Banco de Dados*/
    @Override
    public List<Bem> Lista(int exportado) {

        Integer BemID, ClienteIDFK, SecretariaIDFK, CentroCustoIDFK, LocalizacaoIDFK, ResponsavelIDFK,  ItemIDFK, FabricanteIDFK, EstadoConservacaoIDFK, TipoTomboIDFK, Exportado ;
        String  Plaqueta, Especificacao, Observacao;
        String Valor;
        String Foto1, Foto2;

        List<Bem> bens = new ArrayList<>();
        String[] args = { String.valueOf( clienteIDFK ), String.valueOf( exportado ) };

        String sql = "SELECT * FROM " + ConfiguracaoSQLite.TABELA_BEM + " WHERE ClienteIDFk = ? AND Exportado = ? ;";
        Cursor cursor = Ler.rawQuery(sql,args);

        while (cursor.moveToNext()){

            Bem bem = new Bem();

            BemID = cursor.getInt(cursor.getColumnIndex("BemID"));
            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            Plaqueta = cursor.getString(cursor.getColumnIndex("Plaqueta"));
            SecretariaIDFK = cursor.getInt(cursor.getColumnIndex("SecretariaIDFK"));
            CentroCustoIDFK = cursor.getInt(cursor.getColumnIndex("CentroCustoIDFK"));
            LocalizacaoIDFK = cursor.getInt(cursor.getColumnIndex("LocalizacaoIDFK"));
            ResponsavelIDFK = cursor.getInt(cursor.getColumnIndex("ResponsavelIDFK"));
            ItemIDFK = cursor.getInt(cursor.getColumnIndex("ItemIDFK"));
            Especificacao = cursor.getString(cursor.getColumnIndex("Especificacao"));
            FabricanteIDFK = cursor.getInt(cursor.getColumnIndex("FabricanteIDFK"));
            Valor = cursor.getString(cursor.getColumnIndex("Valor"));
            EstadoConservacaoIDFK = cursor.getInt(cursor.getColumnIndex("EstadoConservacaoIDFK"));
            TipoTomboIDFK = cursor.getInt(cursor.getColumnIndex("TipoTomboIDFK"));
            Observacao = cursor.getString(cursor.getColumnIndex("Observacao"));
            Foto1 = cursor.getString(cursor.getColumnIndex("Foto1"));
            Foto2 = cursor.getString(cursor.getColumnIndex("Foto2"));
            Exportado = cursor.getInt(cursor.getColumnIndex("Exportado"));

            bem.setBemID(BemID);
            bem.setClienteIDFK(ClienteIDFK);
            bem.setPlaqueta(Plaqueta);
            bem.setSecretariaIDFK(SecretariaIDFK);
            bem.setCentroCustoIDFK(CentroCustoIDFK);
            bem.setLocalizacaoIDFK(LocalizacaoIDFK);
            bem.setResponsavelIDFK(ResponsavelIDFK);
            bem.setItemIDFK(ItemIDFK);
            bem.setEspecificacao(Especificacao);
            bem.setFabricanteIDFK(FabricanteIDFK);
            bem.setValor(Valor);
            bem.setEstadoConservacaoIDFK(EstadoConservacaoIDFK);
            bem.setTipoTomboIDFK(TipoTomboIDFK);
            bem.setObservacao(Observacao);
            bem.setFoto1(Foto1);
            bem.setFoto2(Foto2);
            bem.setExportado(Exportado);

            bens.add(bem);
        }

        return bens;
    }

    /**Metodos Responsavel Pesquisa Plaquetas
     @param exportado Indica se a pequisa e feita com bens ja exportados ou não
     @param plaqueta Plaqueta a se pesquisada
     @return Plaquetas*/
    public List<Bem> Pesquisa (String plaqueta, int exportado) {

        Integer BemID, ClienteIDFK, SecretariaIDFK, CentroCustoIDFK, LocalizacaoIDFK, ResponsavelIDFK,  ItemIDFK, FabricanteIDFK, EstadoConservacaoIDFK, TipoTomboIDFK, Exportado ;
        String  Plaqueta, Especificacao, Observacao;
        String Valor;
        String Foto1, Foto2;

        List<Bem> bens = new ArrayList<>();

        Cursor cursor;

        String[] args = { String.valueOf( clienteIDFK ) ,"%" + plaqueta + "%" , String.valueOf( exportado )};

        String sql;

        plaqueta = plaqueta.toUpperCase();

        if (plaqueta.length() > 0 ) {

            if (plaqueta.matches(".*[A-Z].*")) {

                    sql = " SELECT b.* "
                        + " FROM " + ConfiguracaoSQLite.TABELA_BEM + " b "
                        + "      INNER JOIN " + ConfiguracaoSQLite.TABELA_ITEM + " i ON i.ItemID = b.ItemIDFK"
                                                                               + "  AND i.ClienteIDFK = b.ClienteIDFK"
                        + " WHERE b.ClienteIDFK = ? "
                        + "       AND Upper(i.Descricao) LIKE ? "
                        + "       AND Exportado = ? ";

            } else {
                    sql = " SELECT *  FROM " + ConfiguracaoSQLite.TABELA_BEM + " WHERE ClienteIDFK = ? AND Plaqueta LIKE ? AND Exportado = ?;";
            }

            cursor = Ler.rawQuery(sql,args);

        }else  {

            args = new String[]{ String.valueOf( clienteIDFK ), String.valueOf( exportado )};

            sql = " SELECT *  FROM " + ConfiguracaoSQLite.TABELA_BEM + " WHERE ClienteIDFK = ? AND Exportado = ?;";

            cursor = Ler.rawQuery(sql,args);
        }

        while (cursor.moveToNext()) {

            Bem bem = new Bem();

            BemID = cursor.getInt(cursor.getColumnIndex("BemID"));
            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            Plaqueta = cursor.getString(cursor.getColumnIndex("Plaqueta"));
            SecretariaIDFK = cursor.getInt(cursor.getColumnIndex("SecretariaIDFK"));
            CentroCustoIDFK = cursor.getInt(cursor.getColumnIndex("CentroCustoIDFK"));
            LocalizacaoIDFK = cursor.getInt(cursor.getColumnIndex("LocalizacaoIDFK"));
            ResponsavelIDFK = cursor.getInt(cursor.getColumnIndex("ResponsavelIDFK"));
            ItemIDFK = cursor.getInt(cursor.getColumnIndex("ItemIDFK"));
            Especificacao = cursor.getString(cursor.getColumnIndex("Especificacao"));
            FabricanteIDFK = cursor.getInt(cursor.getColumnIndex("FabricanteIDFK"));
            Valor = cursor.getString(cursor.getColumnIndex("Valor"));
            EstadoConservacaoIDFK = cursor.getInt(cursor.getColumnIndex("EstadoConservacaoIDFK"));
            TipoTomboIDFK = cursor.getInt(cursor.getColumnIndex("TipoTomboIDFK"));
            Observacao = cursor.getString(cursor.getColumnIndex("Observacao"));
            Foto1 = cursor.getString(cursor.getColumnIndex("Foto1"));
            Foto2 = cursor.getString(cursor.getColumnIndex("Foto2"));
            Exportado = cursor.getInt(cursor.getColumnIndex("Exportado"));

            bem.setBemID(BemID);
            bem.setClienteIDFK(ClienteIDFK);
            bem.setPlaqueta(Plaqueta);
            bem.setSecretariaIDFK(SecretariaIDFK);
            bem.setCentroCustoIDFK(CentroCustoIDFK);
            bem.setLocalizacaoIDFK(LocalizacaoIDFK);
            bem.setResponsavelIDFK(ResponsavelIDFK);
            bem.setItemIDFK(ItemIDFK);
            bem.setEspecificacao(Especificacao);
            bem.setFabricanteIDFK(FabricanteIDFK);
            bem.setValor(Valor);
            bem.setEstadoConservacaoIDFK(EstadoConservacaoIDFK);
            bem.setTipoTomboIDFK(TipoTomboIDFK);
            bem.setObservacao(Observacao);
            bem.setFoto1(Foto1);
            bem.setFoto2(Foto2);
            bem.setExportado(Exportado);

            bens.add(bem);
        }
        return bens;
    }

    /**Metodos Responsavel Lista Plaqueta Pesquisada
     @param b Bem a ser pesquiadao
     @return Lista de plaqueta do bem pesquidado*/
    public List<Bem> Lista (Bem b) {

        Integer BemID, ClienteIDFK, SecretariaIDFK, CentroCustoIDFK, LocalizacaoIDFK, ResponsavelIDFK,  ItemIDFK, FabricanteIDFK, EstadoConservacaoIDFK, TipoTomboIDFK, Exportado ;
        String  Plaqueta, Especificacao, Observacao;
        String Valor;
        String Foto1, Foto2;

        List<Bem> bens = new ArrayList<>();
        Cursor cursor;
        String[] args = { String.valueOf( clienteIDFK ) };

        String sql = " SELECT * FROM " + ConfiguracaoSQLite.TABELA_BEM + " WHERE ClienteIDFK = ? "
                                                                       + " AND  ( SecretariaIDFK = " + String.valueOf( b.getSecretariaIDFK() ) + " OR " + String.valueOf( b.getSecretariaIDFK() ) + " IS NULL ) "
                                                                       + " AND  ( CentroCustoIDFK = " + String.valueOf( b.getCentroCustoIDFK() ) + " OR " + String.valueOf( b.getCentroCustoIDFK() ) + " IS NULL ) "
                                                                       + " AND  ( LocalizacaoIDFK = " + String.valueOf( b.getLocalizacaoIDFK() ) + " OR " + String.valueOf( b.getLocalizacaoIDFK() ) + " IS NULL ) "
                                                                       + " AND  ( ResponsavelIDFK = " + String.valueOf( b.getResponsavelIDFK() ) + " OR " + String.valueOf( b.getResponsavelIDFK() ) + " IS NULL ) "
                                                                       + " AND  ( ItemIDFK = " + String.valueOf( b.getItemIDFK() ) + " OR " + String.valueOf( b.getItemIDFK() ) + " IS NULL ) "
                                                                       + " AND  ( FabricanteIDFK = " + String.valueOf( b.getFabricanteIDFK() ) + " OR " + String.valueOf( b.getFabricanteIDFK() ) + " IS NULL ) "
                                                                       + " AND  ( EstadoConservacaoIDFK = " + String.valueOf( b.getEstadoConservacaoIDFK() ) + " OR " + String.valueOf( b.getEstadoConservacaoIDFK() ) + " IS NULL ) "
                                                                       + " AND  ( TipoTomboIDFK = " + String.valueOf( b.getTipoTomboIDFK() ) + " OR " + String.valueOf( b.getTipoTomboIDFK() ) + " IS NULL ) " ;

        cursor = Ler.rawQuery(sql,args);

        while (cursor.moveToNext()) {

            Bem bem = new Bem();

            BemID = cursor.getInt(cursor.getColumnIndex("BemID"));
            ClienteIDFK = cursor.getInt(cursor.getColumnIndex("ClienteIDFK"));
            Plaqueta = cursor.getString(cursor.getColumnIndex("Plaqueta"));
            SecretariaIDFK = cursor.getInt(cursor.getColumnIndex("SecretariaIDFK"));
            CentroCustoIDFK = cursor.getInt(cursor.getColumnIndex("CentroCustoIDFK"));
            LocalizacaoIDFK = cursor.getInt(cursor.getColumnIndex("LocalizacaoIDFK"));
            ResponsavelIDFK = cursor.getInt(cursor.getColumnIndex("ResponsavelIDFK"));
            ItemIDFK = cursor.getInt(cursor.getColumnIndex("ItemIDFK"));
            Especificacao = cursor.getString(cursor.getColumnIndex("Especificacao"));
            FabricanteIDFK = cursor.getInt(cursor.getColumnIndex("FabricanteIDFK"));
            Valor = cursor.getString(cursor.getColumnIndex("Valor"));
            EstadoConservacaoIDFK = cursor.getInt(cursor.getColumnIndex("EstadoConservacaoIDFK"));
            TipoTomboIDFK = cursor.getInt(cursor.getColumnIndex("TipoTomboIDFK"));
            Observacao = cursor.getString(cursor.getColumnIndex("Observacao"));
            Foto1 = cursor.getString(cursor.getColumnIndex("Foto1"));
            Foto2 = cursor.getString(cursor.getColumnIndex("Foto2"));
            Exportado = cursor.getInt(cursor.getColumnIndex("Exportado"));

            bem.setBemID(BemID);
            bem.setClienteIDFK(ClienteIDFK);
            bem.setPlaqueta(Plaqueta);
            bem.setSecretariaIDFK(SecretariaIDFK);
            bem.setCentroCustoIDFK(CentroCustoIDFK);
            bem.setLocalizacaoIDFK(LocalizacaoIDFK);
            bem.setResponsavelIDFK(ResponsavelIDFK);
            bem.setItemIDFK(ItemIDFK);
            bem.setEspecificacao(Especificacao);
            bem.setFabricanteIDFK(FabricanteIDFK);
            bem.setValor(Valor);
            bem.setEstadoConservacaoIDFK(EstadoConservacaoIDFK);
            bem.setTipoTomboIDFK(TipoTomboIDFK);
            bem.setObservacao(Observacao);
            bem.setFoto1(Foto1);
            bem.setFoto2(Foto2);
            bem.setExportado(Exportado);

            bens.add(bem);
        }
        return bens;
    }

}
