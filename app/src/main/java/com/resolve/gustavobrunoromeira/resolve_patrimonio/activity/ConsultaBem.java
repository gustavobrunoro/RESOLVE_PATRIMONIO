package com.resolve.gustavobrunoromeira.resolve_patrimonio.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.CentroCustoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.EstadoConsevacaoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.FabricanteDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.ItemDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.LocalizacaoDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.ResponsavelDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.SecretariaDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.DAO.TipoTomboDAO;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Conexao.Database.ConfiguracaoSharedPreferences;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Bem;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.CentroCusto;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.EstadoConservacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Fabricante;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Item;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Localizacao;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Responsavel;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.Secretaria;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.Model.TipoTombo;
import com.resolve.gustavobrunoromeira.resolve_patrimonio.R;

import java.util.ArrayList;
import java.util.List;

public class ConsultaBem extends AppCompatActivity {

    private Button button ;

    private Toolbar toolbar;

    private Spinner secretariaID;
    private Spinner centrocustoID;
    private Spinner localizacaooID;
    private Spinner responsavelID;
    private Spinner itemID;
    private Spinner fabricanteID;
    private Spinner tipoTomboID;
    private Spinner estadoConservacaoID;

    private Bem bem = new Bem();
    private List<Bem> Bens = new ArrayList<>();
    private List<Secretaria> Secretarias = new ArrayList<>();
    private List<CentroCusto> CentroCustos = new ArrayList<>();
    private List<Localizacao> Localizacoes = new ArrayList<>();
    private List<Responsavel> Responsaveis = new ArrayList<>();
    private List<Item> Itens = new ArrayList<>();
    private List<Fabricante> Fabricantes = new ArrayList<>();
    private List<TipoTombo> TipoTombos = new ArrayList<>();
    private List<EstadoConservacao> EstadoConversacoes = new ArrayList<>();

    private ConfiguracaoSharedPreferences preferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_bem);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.Nav2);
        setSupportActionBar(toolbar);

        secretariaID = findViewById(R.id.sp_SecretariaID);
        centrocustoID = findViewById(R.id.sp_CentroCustoID);
        localizacaooID = findViewById(R.id.sp_LocalizacaoID);
        responsavelID = findViewById(R.id.sp_ResponsavelID);
        itemID = findViewById(R.id.sp_ItemID);
        fabricanteID = findViewById(R.id.sp_FabricanteID);
        tipoTomboID = findViewById(R.id.sp_TomboID);
        estadoConservacaoID = findViewById(R.id.sp_ConservacaoID);
        button = findViewById(R.id.btConsultaID);

        listaSecretaria();
        listaCentroCusto();
        listaLocalizacao();
        listaResponsavel();
        listaItem();
        listaFabricante();
        listaTipoTombo();
        listaEstadoConservacao();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),Listagem_Item_Consulta.class);
                intent.putExtra("Bem", bem);
                startActivity(intent);
            }
        });

    }

    /**
     * Metodo responsavel por Lista as Secretaria cadastradas
     *
     * @param secretaria
     */
    public void listaSecretaria() {

        final SecretariaDAO secretariaDAO = new SecretariaDAO(getApplicationContext());
        final List<String> secretariasTemp = new ArrayList<>();

        Secretarias.clear();

        Secretarias = secretariaDAO.Lista();

        for (Secretaria s : Secretarias) {

            if (secretariasTemp.isEmpty()) {
                secretariasTemp.add( "Secretarias" );
            }

            secretariasTemp.add(String.valueOf(s.getSecretariaID()) + " - " + String.valueOf(s.getDescricao()).toUpperCase());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, secretariasTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        secretariaID.setAdapter(spinnerArrayAdapter);

        secretariaID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setSecretariaIDFK(Secretarias.get(position - 1).getSecretariaID());

                    if (bem.getSecretariaIDFK() != null) {
                        listaCentroCusto();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Centro Custo cadastradas
     *
     * @param controle    Paramentro de controle
     * @param centrocusto Centro Custo da plaqueta ja cadastrada
     */
    public void listaCentroCusto() {

        final CentroCustoDAO centroCustoDAO = new CentroCustoDAO(getApplicationContext());
        final List<String> centroCustoTemp = new ArrayList<>();

        CentroCustos.clear();

        CentroCustos = centroCustoDAO.Lista(bem);

        if (centroCustoTemp.isEmpty()) {
            centroCustoTemp.add( "Centro Custos/Departamentos" );
        }

        for (CentroCusto cc : CentroCustos) {

            centroCustoTemp.add(String.valueOf(cc.getCentroCustoID()) + " - " + String.valueOf(cc.getDescricao()).toUpperCase());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, centroCustoTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        centrocustoID.setAdapter(spinnerArrayAdapter);

        centrocustoID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setCentroCustoIDFK(CentroCustos.get(position - 1).getCentroCustoID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Metodo responsavel por Lista as Localizações cadastradas
     *
     * @param controle    Paramentro de controle
     * @param localizacao Localização da plaqueta ja cadastrada
     */
    public void listaLocalizacao() {

        final LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO(getApplicationContext());
        final List<String> localizacaoTemp = new ArrayList<>();

        Localizacoes.clear();

        Localizacoes = localizacaoDAO.Lista(bem);

        if (localizacaoTemp.isEmpty()) {
            localizacaoTemp.add( "Localização" );
        }

        for (Localizacao lt : Localizacoes) {

            localizacaoTemp.add(String.valueOf(lt.getLocalizacaoID()) + " - " + String.valueOf(lt.getDescricao()).toUpperCase());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, localizacaoTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        localizacaooID.setAdapter(spinnerArrayAdapter);

        localizacaooID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setLocalizacaoIDFK(Localizacoes.get(position - 1).getLocalizacaoID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Responsaveis cadastradas
     *
     * @param controle    Paramentro de controle
     * @param responsavel Reponsavel da plaqueta ja cadastrada
     */
    public void listaResponsavel() {

        final ResponsavelDAO responsavelDAO = new ResponsavelDAO(getApplicationContext());
        final List<String> responsavelTemp = new ArrayList<>();

        Responsaveis.clear();

        Responsaveis = responsavelDAO.Lista(bem);

        if (responsavelTemp.isEmpty()) {
            responsavelTemp.add( "Responsavel" );
        }

        for (Responsavel r : Responsaveis) {

            responsavelTemp.add(String.valueOf(String.valueOf(r.getNome()).toUpperCase()));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, responsavelTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        responsavelID.setAdapter(spinnerArrayAdapter);

        responsavelID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setResponsavelIDFK(Responsaveis.get(position - 1).getMatricula());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Itens cadastradas
     *
     * @param controle Paramentro de controle
     * @param item     Item da plaqueta ja cadastrada
     */
    public void listaItem() {

        final ItemDAO itemDAO = new ItemDAO(getApplicationContext());
        final List<String> itemTemp = new ArrayList<>();

        Itens.clear();

        Itens = itemDAO.Lista(bem);

        if (itemTemp.isEmpty()) {
            itemTemp.add( "Item" );
        }

        for (Item r : Itens) {

            itemTemp.add(String.valueOf(String.valueOf(r.getDescricao()).toUpperCase()));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        itemID.setAdapter(spinnerArrayAdapter);

        itemID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setItemIDFK(Itens.get(position - 1).getItemID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Fabricante cadastradas
     *
     * @param controle   Paramentro de controle
     * @param fabricante Fabricante da plaqueta ja cadastrada
     */
    public void listaFabricante() {

        final FabricanteDAO fabricanteDAO = new FabricanteDAO(getApplicationContext());
        final List<String> fabricanteTemp = new ArrayList<>();

        Fabricantes.clear();

        Fabricantes = fabricanteDAO.Lista(bem);

        if (fabricanteTemp.isEmpty()) {
            fabricanteTemp.add( "Fabricante" );
        }

        for (Fabricante r : Fabricantes) {

            fabricanteTemp.add(String.valueOf(String.valueOf(r.getNome()).toUpperCase()));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fabricanteTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        fabricanteID.setAdapter(spinnerArrayAdapter);

        fabricanteID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setFabricanteIDFK(Fabricantes.get(position - 1).getFabricanteID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Tipo Tombo cadastradas
     *
     * @param controle  Paramentro de controle
     * @param tipoTombo Tipo do Tombo da plaqueta ja cadastrada
     */
    public void listaTipoTombo() {

        final TipoTomboDAO tipoTomboDAO = new TipoTomboDAO(getApplicationContext());
        final List<String> tipotomboTemp = new ArrayList<>();

        TipoTombos.clear();

        TipoTombos = tipoTomboDAO.Lista();

        if (tipotomboTemp.isEmpty()) {
            tipotomboTemp.add( "Tipo Tombo" );
        }

        for (TipoTombo tb : TipoTombos) {

            tipotomboTemp.add(String.valueOf(String.valueOf(tb.getDescricao()).toUpperCase()));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipotomboTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        tipoTomboID.setAdapter(spinnerArrayAdapter);

        tipoTomboID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setTipoTomboIDFK(TipoTombos.get(position - 1).getTipoTomboID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Metodo responsavel por Lista os Estado Conservacao cadastradas
     *
     * @param controle          Paramentro de controle
     * @param estadoConservacao Estado de Conservação da plaqueta ja cadastrada
     */
    public void listaEstadoConservacao() {

        final EstadoConsevacaoDAO estadoConsevacaoDAO = new EstadoConsevacaoDAO(getApplicationContext());
        final List<String> estadoConservacaoTemp = new ArrayList<>();

        EstadoConversacoes.clear();

        EstadoConversacoes = estadoConsevacaoDAO.Lista();

        if (estadoConservacaoTemp.isEmpty()) {
            estadoConservacaoTemp.add( "Estado de Conservação" );
        }

        for (EstadoConservacao ec : EstadoConversacoes) {

            estadoConservacaoTemp.add(String.valueOf(String.valueOf(ec.getDescricao()).toUpperCase()));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estadoConservacaoTemp) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        estadoConservacaoID.setAdapter(spinnerArrayAdapter);

        estadoConservacaoID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    bem.setEstadoConservacaoIDFK(EstadoConversacoes.get(position - 1).getEstadoConservacaoID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
