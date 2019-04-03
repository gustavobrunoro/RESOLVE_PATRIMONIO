package com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.CentroCustoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.SecretariaDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCusto;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Secretaria;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterCentroCusto extends RecyclerView.Adapter<AdapterCentroCusto.MyviewHolder> {

    Context context;

    private List<CentroCusto> centroCustos;
    private CentroCusto centroCusto = new CentroCusto();

    public AdapterCentroCusto(List<CentroCusto> listaCentroCusto, Context context) {
        this.centroCustos = listaCentroCusto;
        this.context = context;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_centrocusto_expansivel,parent,false);

        return new AdapterCentroCusto.MyviewHolder(lista);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        String Respo;

        centroCusto = centroCustos.get(position);

        Respo = descricaoSecretario(centroCusto.getSecretariaIDFK());

        if (Respo == null)
            Respo = "";

        holder.expandableCardView.setTitle( String.valueOf( centroCusto.getCentroCustoID()) + " - " + centroCusto.getDescricao().toUpperCase());
        holder.Secretaria.setText( descricaoSecretaria( centroCusto.getSecretariaIDFK() ) );
        holder.Secretario.setText( Respo );
        holder.Responsavel.setText( descricaoReponsavel( centroCusto.getResponsavelIDFK() ) );

    }

    @Override
    public int getItemCount() {
        return centroCustos.size();
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        ExpandableCardView expandableCardView;
        TextView Secretaria, Secretario, Responsavel;

        public MyviewHolder(final View itemView) {
            super(itemView);

            expandableCardView = itemView.findViewById(R.id.cardview_expan_centrocusto);
            Secretaria = itemView.findViewById(R.id.tv_CentroCustoSecretaria);
            Secretario = itemView.findViewById(R.id.tv_CentroCustoSecrtariaResponsavel);
            Responsavel = itemView.findViewById(R.id.tv_CentroCustoResponsavel);

        }
    }

    /**Metodos Responsavel por recupera a descricao da Secretaria
     @return Descricao da Secretaria*/
    public String descricaoSecretaria(int SecretariaIDFK){

        SecretariaDAO secretariaDAO = new SecretariaDAO(context);
        List<Secretaria> secretarias = new ArrayList<>();

        secretarias = secretariaDAO.Pesquisa( String.valueOf( SecretariaIDFK ) );

        return secretarias.get(0).getDescricao();

    }

    /**Metodos Responsavel por recupera a descricao da Secretario
     @return Descricao da Secretario*/
    public String descricaoSecretario(int SecretariaIDFK){

        SecretariaDAO secretariaDAO = new SecretariaDAO(context);
        List<Secretaria> secretarias = new ArrayList<>();

        secretarias = secretariaDAO.Pesquisa( String.valueOf( SecretariaIDFK ) );

        return secretarias.get(0).getNomeSecretario();

    }

    /**Metodos Responsavel por recupera a descricao do Responsavel
     @return Descricao da Responsavel*/
    public String descricaoReponsavel(int ResponsavelIDFK){

        String Reponsavel;
        CentroCustoDAO centroCustoDAO = new CentroCustoDAO(context);
        List<CentroCusto> cc = new ArrayList<>();

        Reponsavel = centroCustoDAO.PesquisaReponsavel( String.valueOf( ResponsavelIDFK ) );

        return Reponsavel;

    }

}
