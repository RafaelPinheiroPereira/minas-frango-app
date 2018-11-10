package com.br.minasfrango.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.br.minasfrango.R;
import com.br.minasfrango.activity.adapter.ClienteAdapter;
import com.br.minasfrango.activity.adapter.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.dao.ClienteDAO;
import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.util.SessionManager;

import java.util.ArrayList;

public class ClienteActivity extends BaseActivity implements RecyclerViewOnClickListenerHack {
		Toolbar mToolbar;
		ClienteDAO clienteDAO = null;
		private LinearLayout ln;
		Bundle savedInstanceState;
		private RecyclerView clienteRecyclerView;
		ClienteAdapter adapter;
		
		@SuppressLint("MissingSuperCall")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
				
				session = new SessionManager(getApplicationContext());
				if (session.checkLogin()) {
						finish();
				}
				
				setDrawer(savedInstanceState, R.layout.activity_main, "Clientes");
				initDrawer(savedInstanceState);
				clienteDAO = ClienteDAO.getInstace();
				
				initViews();
		}
		
		@Override
		protected void onStart() {
				super.onStart();
				LinearLayoutManager layoutManager = new LinearLayoutManager(ClienteActivity.this);
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
				adapter.setRecyclerViewOnClickListenerHack(this);
				clienteRecyclerView.setAdapter(adapter);
		}
		
		private void initViews() {
				clienteRecyclerView = (RecyclerView) findViewById(R.id.recycleview_cliente);
				
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
								Cliente cliente =(Cliente) adapter.getItem(position);
								intent.putExtra("keyCliente", cliente);
								startActivity(intent);
								break;
						
				}
				
		}
		
		@Override
		public void onLongPressClickListener(View view, int position) {
		
		}
}
