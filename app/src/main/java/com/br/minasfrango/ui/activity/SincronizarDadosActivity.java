package com.br.minasfrango.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import com.br.minasfrango.R;
import com.br.minasfrango.data.dto.ItemPedidoDTO;
import com.br.minasfrango.data.dto.PedidoDTO;
import com.br.minasfrango.data.pojo.ItemPedido;
import com.br.minasfrango.data.pojo.Pedido;
import com.br.minasfrango.network.tasks.DataExport;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

public class SincronizarDadosActivity extends AppCompatActivity implements View.OnClickListener {
		
		Button btnImportar, btnExportar;
		Realm realm;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_sincronizar_dados);
            Toolbar toolbar = findViewById(R.id.toolbar);
				toolbar.setTitle("SINCRONIZAÇÃO DE DADOS");
				setSupportActionBar(toolbar);
				
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				
				initView();
				
				btnImportar.setOnClickListener(this);
				btnExportar.setOnClickListener(this);
				
				
		}
		
		@Override
		public void onClick(View v) {

				switch (v.getId()) {
						case R.id.btn_importar:
								//chamar funcao importacao

								try {
//										DataImport dataImport = new DataImport(SincronizarDadosActivity.this, new Funcionario());
//										dataImport.execute();


                                } catch (final Exception e) {
										SincronizarDadosActivity.this.runOnUiThread(new Runnable() {
												public void run() {
                                                    Toast.makeText(SincronizarDadosActivity.this, e.getMessage(),
                                                            Toast.LENGTH_LONG).show();

												}
										});

                                }

                            break;

                    case R.id.btn_exportar:
								realm = Realm.getDefaultInstance();
								RealmQuery<Pedido> query = realm.where(Pedido.class);
								RealmResults<Pedido> results = query.findAll();

                        if (results.size() > 0) {
										exportaDados(results);
								} else {
										Toast.makeText(SincronizarDadosActivity.this, "Dados Inexistentes para o periodo informado!", Toast.LENGTH_LONG).show();
								}
								break;
				}

        }

    private void initView() {

        btnImportar = findViewById(R.id.btn_importar);
        btnExportar = findViewById(R.id.btn_exportar);


    }
		
		private void exportaDados(RealmResults<Pedido> results) {
				
				List<Pedido> pedidos = new ArrayList<Pedido>();
				
				Pedido pedido;
				ItemPedido itemPedido;
				RealmList<ItemPedido> itemPedidos;
				List<ItemPedidoDTO> itemPedidoDTOS = new ArrayList<>();
				List<PedidoDTO> pedidoDTOS = new ArrayList<>();
				PedidoDTO pedidoDTO;
				for (int i = 0; i < results.size(); i++) {
						
						pedido = new Pedido();
						itemPedidos = new RealmList<ItemPedido>();
						pedido.setId(results.get(i).getId());
						pedido.setCodigoFuncionario(results.get(i).getCodigoFuncionario());
						pedido.setCodigoCliente(results.get(i).getCodigoCliente());
						pedido.setDataPedido(results.get(i).getDataPedido());
						pedido.setValorTotal(results.get(i).getValorTotal());
						pedido.setTipoRecebimento(results.get(i).getTipoRecebimento());
						
						for (int j = 0; j < results.get(i).getItens().size(); j++) {
								itemPedido = new ItemPedido();
								itemPedido.setDescricao(results.get(i).getItens().get(j).getDescricao());
								itemPedido.setId(results.get(i).getItens().get(j).getId());
								itemPedido.setValorUnitario(results.get(i).getItens().get(j).getValorUnitario());
								itemPedido.setChavesItemPedido(results.get(i).getItens().get(j).getChavesItemPedido());
								itemPedido.setQuantidade(results.get(i).getItens().get(j).getQuantidade());
								
								ItemPedidoDTO itemPedidoDTO = ItemPedidoDTO.transformaEmItemPedidoDTO(itemPedido);
								itemPedidoDTOS.add(itemPedidoDTO);
								itemPedidos.add(itemPedido);
								
						}
						pedidoDTO = PedidoDTO.transformaEmPedidoDTO(pedido);
						pedidoDTO.setItens(itemPedidoDTOS);
						pedidoDTOS.add(pedidoDTO);
						pedido.setItens(itemPedidos);
						pedidos.add(pedido);
						
						
				}
				
				DataExport task = new DataExport(SincronizarDadosActivity.this, pedidoDTOS);
				task.execute();
				
				
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
				int id = item.getItemId();
				
				switch (id) {
						
						// Id correspondente ao botão Up/HomeActivity da actionbar
						case android.R.id.home:
								NavUtils.navigateUpFromSameTask(this);
								break;
				}
				
				return super.onOptionsItemSelected(item);
		}
}
