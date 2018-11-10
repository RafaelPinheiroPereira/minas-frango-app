package com.br.minasfrango.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.br.minasfrango.R;
import com.br.minasfrango.activity.adapter.ImportacaoDadosAdapter;
import com.br.minasfrango.model.Funcionario;
import com.br.minasfrango.model.ImportacaoDados;
import com.br.minasfrango.util.ImportData;

import java.util.ArrayList;


public class ImportacaoActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    ImportacaoDadosAdapter dataAdapter = null;
    Button btnImportar;
    ArrayList<ImportacaoDados> importacaoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Importação de Dados");
        setSupportActionBar(toolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();


    }

    private void initView() {
        importacaoDados = new ArrayList<ImportacaoDados>();
        ImportacaoDados importacao = new ImportacaoDados("1","Clientes",false);
        importacaoDados.add(importacao);
        importacao = new ImportacaoDados("2","Forma de Pagamento",false);
        importacaoDados.add(importacao);
        importacao = new ImportacaoDados("3","Unidades",false);
        importacaoDados.add(importacao);
        importacao = new ImportacaoDados("4","Produtos",false);
        importacaoDados.add(importacao);
        importacao = new ImportacaoDados("5","Preços",false);
        importacaoDados.add(importacao);


        //create an ArrayAdaptar from the String Array
        dataAdapter = new ImportacaoDadosAdapter(this,
                R.layout.item_importacao_dados, importacaoDados);
        listView = (ListView) findViewById(R.id.list_importacao);
        btnImportar=(Button) findViewById(R.id.btn_importar);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        btnImportar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_importar:
                //chamar funcao importacao

               
               try {ImportData importData= new ImportData(ImportacaoActivity.this,importacaoDados,new Funcionario());
                   importData.execute();

               }catch (final Exception e){
                   ImportacaoActivity.this.runOnUiThread(new Runnable() {
                       public void run() {
                           Toast.makeText(ImportacaoActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();

                       }
                   });

               }

                break;
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){


            // Id correspondente ao botão Up/Home da actionbar
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
