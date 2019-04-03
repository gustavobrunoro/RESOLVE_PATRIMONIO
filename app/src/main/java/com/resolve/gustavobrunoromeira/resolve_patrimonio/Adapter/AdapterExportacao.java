package com.resolve.gustavobrunoromeira.resolve_patrimonio.Adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.ItemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Item;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterExportacao extends RecyclerView.Adapter<AdapterExportacao.MyviewHolder> {

    private Context context;
    private String clienteIDFK= "99";
    private String caminhoFotoPrincipal = "/Resolve Patrimonio/" + clienteIDFK + "/Fotos/";
    private Boolean Selecao= false;

    private List<Bem> bens;
    private List<Bem> bensSelecionados = new ArrayList<>();
    private Bem bem = new Bem();

    public AdapterExportacao(List<Bem> listaItem, Context context) {
        this.bens = listaItem;
        this.context = context;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_bem,parent,false);

        return new AdapterExportacao.MyviewHolder(lista);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, final int position) {

        final CheckBox expotar = holder.Exportacao;
        bem = bens.get(position);

        Picasso.with( context )
                .load( new File(Environment.getExternalStorageDirectory().getAbsolutePath(), caminhoFotoPrincipal + bem.getPlaqueta() + "/" + bem.getPlaqueta() + "_1.png"))
                .placeholder( R.drawable.ic_camera_24dp )
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error( R.drawable.ic_camera_24dp )
                .into( holder.Foto );

        holder.Plaqueta.setText("Plaqueta: " + bem.getPlaqueta());
        holder.Descricao.setText(descricaoItem());

        // Habilita o check box de Exportação
        holder.Exportacao.setVisibility(View.VISIBLE);

        // Verificar se Selecionou todos os Itens, Caso tenha Selecionado todas as CheckBox são marcadas
        if (!Selecao) {
            holder.Exportacao.setChecked(false);
        }
        else{
            holder.Exportacao.setChecked(true);
        }

        // Efetua a Ação de Add ou Remove o Item da lista a Ser Enviada
        holder.Exportacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Caso o Item Seja Marcado o Item e ADD
                if (expotar.isChecked()){
                    bensSelecionados.add( bens.get( position ) );
                 }

                //Caso o Item Seja Desmarcado o Item e Removido
                if (expotar.isChecked() == false ){
                    bensSelecionados.remove( bens.get( position ) );
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bens.size();
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder {

        private ImageView Foto;
        private TextView Descricao,Plaqueta;
        private CheckBox Exportacao;

        public MyviewHolder(final View itemView) {
            super(itemView);

            Descricao  = itemView.findViewById(R.id.tv_DescricaoItemID);
            Plaqueta   = itemView.findViewById(R.id.tv_PlaquetaItemID);
            Foto       = itemView.findViewById(R.id.iv_FotoID);
            Exportacao = itemView.findViewById(R.id.cb_ExportacaoID);

        }

    }

    /**Metodo que Retorna a Descricao do Item
     * @return Descricao */
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

    /**Metodo que Marca todos os Itens a serem Enviado */
    public void marcaTudo(){

        Selecao = true;
        notifyDataSetChanged();

        bensSelecionados.clear();
        bensSelecionados.addAll(bens);

    }

    /**Metodo que Desmarca todos os Itens a serem Enviado */
    public void desmarcaTudo(){

        Selecao = false;
        notifyDataSetChanged();

        if (!bensSelecionados.isEmpty()){
            bensSelecionados.clear();
        }

    }

    /**Metodo que Retorna a  Lista de Bens Selecionados para Exportação
     * @return  Lista de bem */
    public List<Bem> ListaSelecionados (){

        return bensSelecionados;

    }

}
