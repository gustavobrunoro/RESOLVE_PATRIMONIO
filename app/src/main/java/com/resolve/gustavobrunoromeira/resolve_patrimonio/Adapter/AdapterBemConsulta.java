package com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.CardView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.powerapps.powerimagecompress.PowerImageCompress;

public class AdapterBemConsulta extends RecyclerView.Adapter<AdapterBemConsulta.MyviewHolder> {

    Context context;
    File caminhoFoto;

    private List<Bem> bens;
    private Bem bem = new Bem();

    public AdapterBemConsulta(List<Bem> listaItem, Context context) {
        this.bens = listaItem;
        this.context = context;
    }

    @Override
    public MyviewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_bem,parent,false);

        return new AdapterBemConsulta.MyviewHolder(lista);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        bem = bens.get(position);

        // Seleciona o Caminho da Foto 1
        caminhoFoto = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Resolve Patrimonio/" + bem.getClienteIDFK() + "/Fotos/" + bem.getPlaqueta() + "_1" +".png");

        // Verificar se a Foto 1 Existe
        if (caminhoFoto.exists()) {

            // Seta o Image View da Activity do com a Foto
            ImageView mImageView1 = PowerImageCompress.doArquivo(caminhoFoto)
                    .exibirEm(holder.Foto);
        }

        holder.Plaqueta.setText("Plaqueta: " + bem.getPlaqueta());
        holder.Descricao.setText(descricaoItem());

        if (bem.getExportado() == 1){
            holder.Exportado.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return bens.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        private ImageView Foto,Exportado;
        private TextView Descricao,Plaqueta;
        private CardView cardView;


        public MyviewHolder(final View itemView) {
            super(itemView);

            Descricao = itemView.findViewById(R.id.tv_DescricaoItemID);
            Plaqueta  = itemView.findViewById(R.id.tv_PlaquetaItemID);
            cardView  = itemView.findViewById(R.id.CardViewBemID);
            Foto      = itemView.findViewById(R.id.iv_FotoID);
            Exportado = itemView.findViewById(R.id.expotadoID);

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
