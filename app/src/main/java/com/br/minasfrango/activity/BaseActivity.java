package com.br.minasfrango.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.br.minasfrango.R;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.Pedido;
import com.br.minasfrango.model.com.br.minasfrango.dto.ItemPedidoDTO;
import com.br.minasfrango.model.com.br.minasfrango.dto.PedidoDTO;
import com.br.minasfrango.util.ExportData;
import com.br.minasfrango.util.SessionManager;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class BaseActivity extends AppCompatActivity {
		
		private Toolbar toolbar;
		private Drawer.Result fullView;
		private AccountHeader.Result headerNavigationLeft;
		
		private LinearLayout ln;
		Bundle savedInstanceState;
		SessionManager session;
		String icon;
		
		protected void initDrawer(Bundle bundle) {
				
				//initialize and create the image loader logic
				DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
						@Override
						public void set(ImageView imageView, Uri uri, Drawable placeholder) {
								Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
						}
						
						@Override
						public void cancel(ImageView imageView) {
								Picasso.with(imageView.getContext()).cancelRequest(imageView);
						}
						
						@Override
						public Drawable placeholder(Context ctx) {
								return null;
						}
				});
				headerNavigationLeft = new AccountHeader()
								.withActivity(BaseActivity.this)
								.withCompactStyle(false)
								.withSavedInstance(savedInstanceState)
								.withHeaderBackground(R.color.colorPrimary)
								.withSelectionFistLineShown(true) // Nome
								.withSelectionSecondLineShown(false) // E-mail
								.addProfiles(
												new ProfileDrawerItem().withName(session.getNome().toString()).withNameShown(true).withIcon(getResources().getDrawable(R.mipmap.ic_profile))
								)
								
								.withAlternativeProfileHeaderSwitching(false)
								.withProfileImagesClickable(true)
								.withSelectionListEnabled(false)
								.build();
				
				fullView = new Drawer()
								.withActivity(this)
								.withToolbar(toolbar)
								.withDisplayBelowToolbar(true)
								.withActionBarDrawerToggleAnimated(true)
								.withDrawerGravity(Gravity.LEFT)
								.withSavedInstance(savedInstanceState)
								.withActionBarDrawerToggle(true)
								.withAccountHeader(headerNavigationLeft)
								.withTranslucentStatusBar(false)
								.withSelectedItem(-1)
								.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
												switch (position) {
														case 0:
																
																break;
														case 1:
																//  startActivity(new Intent(BaseActivity.this,PedidosActivity.class));
																break;
														case 2:
																break;
														case 3:
																startActivity(new Intent(BaseActivity.this, SincronizarDadosActivity.class));
																break;
													
														case 4:
																alertaConfirmacao();
																break;
												}
										}
								})
								.build();
				
				fullView.addItem(new PrimaryDrawerItem().withName("Home").withIcon(getResources().getDrawable(R.mipmap.ic_home)));
				fullView.addItem(new PrimaryDrawerItem().withName("Pedidos").withIcon(getResources().getDrawable(R.mipmap.ic_cart)));
				fullView.addItem(new PrimaryDrawerItem().withName("Clientes").withIcon(getResources().getDrawable(R.drawable.ic_face_black_24dp)));
				fullView.addItem(new PrimaryDrawerItem().withName("Sincronizar Dados").withIcon(getResources().getDrawable(R.drawable.sicronizacao_dados)));
				fullView.addItem(new PrimaryDrawerItem().withName("Sair").withIcon(getResources().getDrawable(R.mipmap.ic_logout)));
				if (Build.VERSION.SDK_INT >= 19) {
						fullView.getDrawerLayout().setFitsSystemWindows(false);
				}
				
		}
		
		
		
		
		
		protected void setDrawer(Bundle savedInstanceState, int layoutId, String titulo) {
				super.onCreate(savedInstanceState);
				setContentView(layoutId);
				session = new SessionManager(getApplicationContext());
				if (session.checkLogin()) {
						finish();
				}
				ln = (LinearLayout) findViewById(R.id.nav_header_container);
				toolbar = (Toolbar) findViewById(R.id.toolbar);
				
				if (!titulo.equals("")) {
						toolbar.setTitle(titulo);
				} else {
						toolbar.setTitle("");
				}
				setSupportActionBar(toolbar);
				
				
		}
		
		@Override
		protected void onSaveInstanceState(Bundle outState) {
				
				super.onSaveInstanceState(outState);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
				//handle the click on the back arrow click
				switch (item.getItemId()) {
						case android.R.id.home:
								
								return true;
						
						default:
								return super.onOptionsItemSelected(item);
				}
		}
		
		@Override
		public void onBackPressed() {
				//handle the back press :D close the drawer first and if the drawer is closed close the activity
				if (fullView != null && fullView.isDrawerOpen()) {
						fullView.closeDrawer();
				} else {
						super.onBackPressed();
				}
		}
		
		public void alertaConfirmacao() {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
				builder.setTitle("Realizar Logout");
				builder.setMessage("Deseja realmente sair do sistema?");
				
				String positiveText = "SIM";
				builder.setPositiveButton(positiveText,
								new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
												// positive button logic
												
												session.logoutUser();
												dialog.dismiss();
												//edtConfirmaEmail.setText("");
												
												return;
										}
								});
				
				String negativeText = "NÃO";
				builder.setNegativeButton(negativeText,
								new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
												// negative button logic
												return;
										}
								});
				
				AlertDialog dialog = builder.create();
				// display dialog
				dialog.show();
		}
		
		@Override
		protected void onStart() {
				super.onStart();
		}
}

