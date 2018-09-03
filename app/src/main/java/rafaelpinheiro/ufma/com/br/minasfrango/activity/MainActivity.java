package rafaelpinheiro.ufma.com.br.minasfrango.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;

import rafaelpinheiro.ufma.com.br.minasfrango.R;
import util.SessionManager;

public class MainActivity extends BaseActivity {
		Toolbar mToolbar;
		
		private LinearLayout ln;
		Bundle savedInstanceState;
		private RecyclerView clienteRecyclerView;
		
		@SuppressLint("MissingSuperCall")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
				
				
				setDrawer(savedInstanceState, R.layout.activity_main, "Clientes");
				initDrawer(savedInstanceState);
				
				session = new SessionManager(getApplicationContext());
				if (session.checkLogin()) {
						finish();
				}
				
				initViews();
		}
		
		private void initViews() {
				clienteRecyclerView = (RecyclerView) findViewById(R.id.recycleview_cliente);
				
		}
		
}
