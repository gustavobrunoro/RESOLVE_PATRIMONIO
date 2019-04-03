package com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Localizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.util.List;

public class AdapterLocalizacao extends RecyclerView.Adapter<AdapterLocalizacao.MyviewHolder>  {

    Context context;

    private List<Localizacao> localizacoes;
    private Localizacao localizacao = new Localizacao();

    public AdapterLocalizacao(List<Localizacao> listaLocalizacao, Context context) {
        this.localizacoes = listaLocalizacao;
        this.context = context;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_localizacao_expansivel,parent,false);

        return new AdapterLocalizacao.MyviewHolder(lista);
}

    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {

         localizacao = localizacoes.get(position);

        holder.expandableCardView.setTitle( String.valueOf( localizacao.getLocalizacaoID()) + " - " + localizacao.getDescricao());
        holder.Complemento.setText( localizacao.getComplemento() );
        holder.Endereco.setText( "" );
        holder.Numero.setText( "" );
        holder.Bairro.setText( "" );
        //holder.Telefone.setText( localizacao.getTelefone() );
        holder.Telefone.setText( "32811297" );

    }

    @Override
    public int getItemCount() {
        return localizacoes.size();
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        ExpandableCardView expandableCardView;
        TextView Complemento, Endereco, Numero, Bairro, Telefone;

        public MyviewHolder(final View itemView) {
            super(itemView);

            expandableCardView = itemView.findViewById(R.id.cardview_expan_localizacao);
            Complemento = itemView.findViewById(R.id.tv_LocalizacaoComplemento);
            Endereco = itemView.findViewById(R.id.tv_LocalizacaoEndereco);
            Numero = itemView.findViewById(R.id.tv_LocalizacaoNumero);
            Bairro = itemView.findViewById(R.id.tv_LocalizacaoBairro);
            Telefone = itemView.findViewById(R.id.tv_LocalizacaoTelefone);

        }
    }

}
