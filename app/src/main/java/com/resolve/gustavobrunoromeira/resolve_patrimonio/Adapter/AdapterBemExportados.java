package com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.ItemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Item;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterBemExportados extends RecyclerView.Adapter<AdapterBemExportados.MyviewHolder> {

    Context context;

    private List<Bem> bens;
    private Bem bem = new Bem();

    public AdapterBemExportados(List<Bem> listaItem, Context context) {
        this.bens = listaItem;
        this.context = context;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_bem,parent,false);

        return new AdapterBemExportados.MyviewHolder(lista);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        bem = bens.get(position);

        holder.Plaqueta.setText("Plaqueta: " + bem.getPlaqueta());
        holder.Descricao.setText(descricaoItem());

        Picasso.with( context )
               .load( "https://resolveconsultoria.com/patrimonio/Patrimonio/Clientes/" + bem.getClienteIDFK() +  "/" + bem.getPlaqueta() + "/" + bem.getPlaqueta() + "_1.jpg" )
               .placeholder (R.drawable.ic_camera_24dp )
               .error( R.drawable.ic_camera_24dp )
               .into( holder.Foto );

    }

    @Override
    public int getItemCount() {
        return bens.size();
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        private ImageView Foto;
        private TextView Descricao,Plaqueta;

        public MyviewHolder(final View itemView) {
            super(itemView);

            Descricao = itemView.findViewById(R.id.tv_DescricaoItemID);
            Plaqueta  = itemView.findViewById(R.id.tv_PlaquetaItemID);
            Foto      = itemView.findViewById(R.id.iv_FotoID);

        }

    }

    /**Metodos Responsavel por recupera a descricao do Item
     @return Descricao do Item*/
    public String descricaoItem(){

        List<Item> items = new ArrayList<>();
        ItemDAO itemDAO = new ItemDAO(context);

        items = itemDAO.Lista(bem);

        for ( Item item : items ) {

            if (item.getItemID() == bem.getItemIDFK()){

                return item.getDescricao();
            }
        }
        return "";
    }

}
