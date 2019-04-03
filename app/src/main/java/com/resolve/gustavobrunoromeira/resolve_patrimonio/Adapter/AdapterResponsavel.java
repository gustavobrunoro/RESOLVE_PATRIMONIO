package com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.CentroCustoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.LocalizacaoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCusto;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Localizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Responsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterResponsavel extends RecyclerView.Adapter<AdapterResponsavel.MyviewHolder>  {

    Context context;

    private List<Responsavel> responsaveis;
    private Responsavel responsavel = new Responsavel();

    public AdapterResponsavel(List<Responsavel> listaResponsavel, Context context) {
        this.responsaveis = listaResponsavel;
        this.context = context;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_responsavel_expansivel,parent,false);

        return new AdapterResponsavel.MyviewHolder(lista);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        responsavel = responsaveis.get(position);

        holder.expandableCardView.setTitle( String.valueOf( responsavel.getMatricula() ) + " - " + responsavel.getNome() );
        holder.CentroCusto.setText( descricaoCentroCusto( responsavel.getCentroCustoIDFK() ) );
        holder.Localizacao.setText( descricaoLocalizacao( responsavel.getLocalizacaoIDFK() ) );
        holder.Telefone.setText( "11111" );

    }

    @Override
    public int getItemCount() {
        return responsaveis.size();
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        ExpandableCardView expandableCardView;
        TextView CentroCusto, Localizacao, Telefone;


        public MyviewHolder(final View itemView) {
            super(itemView);

            expandableCardView = itemView.findViewById(R.id.cardview_expan_responsavel);
            CentroCusto = itemView.findViewById(R.id.tv_ResponsavelCentroCusto);
            Localizacao = itemView.findViewById(R.id.tv_ResponsavelLocalizacao);
            Telefone = itemView.findViewById(R.id.tv_ResponsavelTelefone);

        }
    }

    /**Metodos Responsavel por recupera a descricao do Centro Custo
     @return Descricao do Centro Custo*/
    public String descricaoCentroCusto(int CentroCustoIDFK){

        CentroCustoDAO centroCustoDAO = new CentroCustoDAO(context);
        List<CentroCusto> centroCustos = new ArrayList<>();

        centroCustos = centroCustoDAO.Pesquisa( String.valueOf( CentroCustoIDFK ) );

        return centroCustos.get(0).getDescricao();
    }

    /**Metodos Responsavel por recupera a descricao da Localização
     @return Descricao da Localização*/
    public String descricaoLocalizacao(int LocalizacaoIDFK){

        LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(context);
        List<Localizacao> localizacoes = new ArrayList<>();

        localizacoes = localizacaoDAO.Pesquisa( String.valueOf( LocalizacaoIDFK ) );

        return localizacoes.get(0).getDescricao();
    }

}
