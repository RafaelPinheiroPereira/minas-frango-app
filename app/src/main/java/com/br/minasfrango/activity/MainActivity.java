package com.br.minasfrango.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.br.minasfrango.R;
import com.br.minasfrango.util.SessionManager;

public class MainActivity extends BaseActivity {
		Toolbar mToolbar;
		
		private LinearLayout ln;
		Bundle savedInstanceState;
		private RecyclerView clienteRecyclerView;
		
		@SuppressLint("MissingSuperCall")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
				
				session = new SessionManager(getApplicationContext());
				if (session.checkLogin()) {
						finish();
				}
				
				
				setDrawer(savedInstanceState, R.layout.activity_main, "Clientes");
				initDrawer(savedInstanceState);
				
			
				initViews();
		}
		
		private void initViews() {
				clienteRecyclerView = (RecyclerView) findViewById(R.id.recycleview_cliente);
				
		}
		
}
