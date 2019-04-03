package com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Secretaria;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.util.List;

public class AdapterSecretaria extends RecyclerView.Adapter<AdapterSecretaria.MyviewHolder> {

    Context context;

    private List<Secretaria> secretarias;
    private Secretaria secretaria = new Secretaria();

    public AdapterSecretaria (List<Secretaria> listaSecretaria, Context context) {
        this.secretarias = listaSecretaria;
        this.context = context;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_secretaria_expansivel,parent,false);

        return new AdapterSecretaria.MyviewHolder(lista);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        String Respo;

        secretaria = secretarias.get(position);
        Respo = secretaria.getNomeSecretario();

        if (Respo == null)
        Respo = "";

        holder.expandableCardView.setTitle( String.valueOf( secretaria.getSecretariaID() )  + " - " + secretaria.getDescricao().toUpperCase());
        holder.Secretario.setText( Respo );
        holder.Endereco.setText( secretaria.getEndereco() );
        holder.Numero.setText( secretaria.getNumero() );
        holder.Bairro.setText( secretaria.getBairro() );

    }

    @Override
    public int getItemCount() {
        return secretarias.size();
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        private TextView Secretario,Endereco,Numero,Bairro;

        ExpandableCardView expandableCardView;

        public MyviewHolder(final View itemView) {
            super(itemView);

            expandableCardView = itemView.findViewById(R.id.cardview_expan_secretaria);
            Secretario = itemView.findViewById(R.id.tv_SecretariaSecretario);
            Endereco = itemView.findViewById(R.id.tv_SecretariaEndereco);
            Numero = itemView.findViewById(R.id.tv_SecretariaNumero);
            Bairro = itemView.findViewById(R.id.tv_SecretariaBairro);

        }
    }

}
