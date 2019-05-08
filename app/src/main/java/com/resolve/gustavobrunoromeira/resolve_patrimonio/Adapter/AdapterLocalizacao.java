package com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alespero.expandablecardview.ExpandableCardView;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Localizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

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
    public void onBindViewHolder(final MyviewHolder holder, final int position) {

         localizacao = localizacoes.get(position);

        holder.expandableCardView.setTitle( String.valueOf( localizacao.getLocalizacaoID()) + " - " + localizacao.getDescricao());
        holder.Complemento.setText( localizacao.getComplemento() );
        holder.Endereco.setText( localizacao.getEndereco() );
        holder.Endereco.setPaintFlags(holder.Endereco.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.Numero.setText( localizacao.getNumero() );
        holder.Bairro.setText( localizacao.getBairro()  );
        holder.Telefone.setText( localizacao.getTelefone() );

        //Chama o Google Mapas para Navegar ate o Endereço
        holder.Endereco.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText( context , localizacoes.get( position ).getEndereco() , Toast.LENGTH_SHORT ).show();

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + localizacoes.get( position ).getEndereco() +","+ localizacoes.get( position ).getNumero()+ "," + localizacoes.get( position ).getBairro() + "," + localizacoes.get( position ).getCidade());
                Intent mapIntent = new Intent( Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                view.getContext().startActivity( mapIntent );

            }
        } );




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
