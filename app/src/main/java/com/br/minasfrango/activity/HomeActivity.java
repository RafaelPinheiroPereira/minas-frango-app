package com.br.minasfrango.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.adapter.ClienteAdapter;
import com.br.minasfrango.data.dao.ClienteDAO;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.dao.RotaDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Rota;
import com.br.minasfrango.listener.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.util.SessionManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends BaseActivity
        implements RecyclerViewOnClickListenerHack, AdapterView.OnItemSelectedListener,
        ClienteAdapter.ClienteAdapterListener {

    ClienteDAO clienteDAO = null;

    ArrayList<Cliente> clienteRota;

    private LinearLayout ln;

    public static final String PREFS_NAME = "Preferences";

    private RecyclerView clienteRecyclerView;

    ClienteAdapter adapter;

    ArrayAdapter adapterRota;

    ArrayList<Rota> rotas;

    private RotaDAO rotaDAO = null;

    Spinner rotaSpinner;

    RecebimentoDAO recebimentoDAO;

    private SearchView searchView;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        session = new SessionManager(getApplicationContext());
        if (session.checkLogin()) {
            finish();
        }

        setDrawer(savedInstanceState, R.layout.activity_main, "TRYINITY MOBILE");
        initDrawer(savedInstanceState);
        clienteDAO = ClienteDAO.getInstace();
        recebimentoDAO = RecebimentoDAO.getInstace();

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        clienteRecyclerView.setLayoutManager(layoutManager);

        ArrayList<Cliente> clientes = clienteDAO.allClientes();
        if (clientes != null) {

            adapter = new ClienteAdapter(this, clientes);
        } else {
            clientes = new ArrayList<Cliente>();
            adapter = new ClienteAdapter(this, clientes);
        }

        rotaDAO = RotaDAO.getInstace();
        rotas = new ArrayList<Rota>();
        rotas = rotaDAO.carregaRota();
        adapterRota = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, rotas);

        rotaSpinner.setAdapter(adapterRota);
        rotaSpinner.setPrompt("Todas as Rotas");

        rotaSpinner.setOnItemSelectedListener(this);
        //Restaura as preferencias gravadas
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        clienteRota = new ArrayList<Cliente>();
        clienteRota.addAll(clientes);

        if (settings.getInt("Rotas", 0) == 0) {
            //seta spinner 0
            rotaSpinner.setSelection(0);
        } else {
            rotaSpinner.setSelection(settings.getInt("Rotas", 0));
            rotaSpinner.setOnItemSelectedListener(this);
        }

        adapter.setRecyclerViewOnClickListenerHack(this);
        clienteRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initViews() {
        clienteRecyclerView = findViewById(R.id.recycleview_cliente);
        rotaSpinner = findViewById(R.id.spn_rota);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Rota rota = (Rota) adapterRota.getItem(position);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Rotas", position);
        //Confirma a gravação dos dados
        editor.commit();
        rotaSpinner.setSelection(position);

        carregaClientesPorRota(rota);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void carregaClientesPorRota(Rota rota) {

        ArrayList<Cliente> aux = new ArrayList<Cliente>();
        for (Cliente cliente : clienteDAO.pesquisarClientePorRota(rota)) {
            aux.add(cliente);
        }
        clienteRota.clear();
        clienteRota = aux;
        adapter.notifyDataSetChanged();
        adapter = new ClienteAdapter(this, clienteRota);

        adapter.setRecyclerViewOnClickListenerHack(this);
        clienteRecyclerView.setAdapter(adapter);

    }

    protected ClienteDAO getInstaceClienteDAO() {
        if (clienteDAO == null) {
            clienteDAO = ClienteDAO.getInstace();
        }
        return clienteDAO;
    }

    @Override
    public void onClickListener(View view, int position) {

        switch (view.getId()) {

            case R.id.btnVender:

                Intent intent = new Intent(this, VendasActivity.class);
                Cliente cliente = adapter.getItem(position);
                Bundle params = new Bundle();
                params.putSerializable("keyCliente", cliente);
                params.putLong("keyPedido",0);
                intent.putExtras(params);
                startActivity(intent);
                break;

            case R.id.btnReceber:

                if (recebimentoDAO.recebimentosPorCliente(adapter.getItem(position)).size() > 0) {
                    Intent receIntent = new Intent(this, RecebimentoActivity.class);
                    Bundle receBundle = new Bundle();
                    receBundle.putSerializable("keyCliente", adapter.getItem(position));
                    receBundle.putString("dataVenda",
                            new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
                    receIntent.putExtras(receBundle);
                    startActivity(receIntent);
                } else {
                    Toast.makeText(HomeActivity.this, "Cliente sem débito!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imgInfo:
                dialogDetalheCliente(adapter.getItem(position));
                break;


        }

    }


    @Override
    public void onLongPressClickListener(View view, int position) {

    }

    private void dialogDetalheCliente(Cliente cliente) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_detalhe_cliente, null);
        dialogBuilder.setView(dialogView);

        TextView codigoClienteTextView;
        TextView razaoSocialTextView, qtdNotasAbertasTextView, valorMedioCompraTextView;
        TextView nomeFantasiaTextView, obsTextView, rgTextView;
        TextView enderecoTextView, pontoReferenciaTextView, cpfcnpjTextView;
        TextView bairroClienteTextView, telefoneTextView, contatoTextView;
        TextView cepClienteTextView, cidadeTextView, codigoLocalidadeTextView;

        //	dialogView.setContentView(R.layout.dialog_detalhe_cliente);

        codigoClienteTextView = dialogView.findViewById(R.id.txt_codigo_cliente);
        razaoSocialTextView = dialogView.findViewById(R.id.txt_razao_social);
        nomeFantasiaTextView = dialogView.findViewById(R.id.txt_nome_fantasia);
        enderecoTextView = dialogView.findViewById(R.id.txt_endereco);
        bairroClienteTextView = dialogView.findViewById(R.id.txt_bairro);
        cepClienteTextView = dialogView.findViewById(R.id.txt_cep);
        cidadeTextView = dialogView.findViewById(R.id.txt_cidade);
        obsTextView = dialogView.findViewById(R.id.txt_observacao);
        rgTextView = dialogView.findViewById(R.id.txt_rg);
        valorMedioCompraTextView = dialogView.findViewById(R.id.txt_vlr_medio_compra);

        codigoLocalidadeTextView = dialogView.findViewById(R.id.txt_codigo_localidade);
        telefoneTextView = dialogView.findViewById(R.id.txt_telefone);
        contatoTextView = dialogView.findViewById(R.id.txt_contato);
        pontoReferenciaTextView = dialogView.findViewById(R.id.txt_ponto_REF);
        cpfcnpjTextView = dialogView.findViewById(R.id.txt_cpf_cnpj);
        qtdNotasAbertasTextView = dialogView.findViewById(R.id.txt_qtd_notas_abertas);
        // Custom Android Allert Dialog Title
        dialogBuilder.setTitle("Informações do Cliente");
        nomeFantasiaTextView.setText(cliente.getNome());
        razaoSocialTextView.setText(cliente.getRazaoSocial());
        codigoClienteTextView.setText(String.valueOf(cliente.getId()));
        enderecoTextView.setText(cliente.getEndereco());
        bairroClienteTextView.setText(cliente.getBairro());
        cepClienteTextView.setText(String.valueOf(cliente.getCep()));
        cidadeTextView.setText(cliente.getCidade());
        telefoneTextView.setText(cliente.getTelefone());
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_cliente_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClienteSelected(Cliente cliente) {

    }
}
