package com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.BemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.util.List;

public class AdapterPrincipal extends RecyclerView.Adapter<AdapterPrincipal.MyviewHolder> {

    private List<Bem> bemsCadastrados;
    private Integer bemsExportados;
    private BemDAO bemDAO;

    public AdapterPrincipal(Context context, Integer TotalBens) {
        bemDAO = new BemDAO(context);
        bemsCadastrados = bemDAO.Lista(0);
        bemsExportados = TotalBens;
    }

    @Override
    public AdapterPrincipal.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View  lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_principal,parent,false);

        return new MyviewHolder(lista);

    }

    @Override
    public void onBindViewHolder(AdapterPrincipal.MyviewHolder holder, int position) {

        String total;

        if (position == 0 ){

            if(bemsCadastrados == null){
                total = "0";
            }else {
                total = String.valueOf( bemsCadastrados.size() );
            }

            holder.Descricao.setText(R.string.Titulo1);
            holder.Itens.setText( total );

        }else{

            if( bemsExportados == 0 ){
                total = "0";
            }else {
                total = String.valueOf( bemsExportados );
            }

            holder.Descricao.setText(R.string.Titulo2);
            holder.Itens.setText( total );
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        private TextView Descricao,Itens;

        public MyviewHolder(final View itemView) {
            super(itemView);

            Descricao = itemView.findViewById(R.id.tv_desccricaoID);
            Itens = itemView.findViewById(R.id.tv_ItensID);

        }
    }

    /**Metodos Responsavel por recupera o Total de Item Cadastrados
     @return Total de Item*/
    public int totalBemCadastrados(){
        return bemsCadastrados.size();
    }

}
