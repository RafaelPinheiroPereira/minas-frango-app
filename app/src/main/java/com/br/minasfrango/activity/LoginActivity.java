package com.br.minasfrango.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.br.minasfrango.R;
import com.br.minasfrango.model.Funcionario;
import com.br.minasfrango.service.LoginService;
import com.br.minasfrango.util.RetrofitConfig;
import com.br.minasfrango.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
		SessionManager session;
		
		LinearLayout loginBox;
		Button submit;
		ImageView imgLogo;
		EditText edtMatricula;
		EditText edtSenha;
		private static int REQUEST_STORAGE = 112;
		private static String[] PERMISSIONS = {Manifest.permission.BLUETOOTH,
						Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_PRIVILEGED, Manifest.permission.INTERNET};
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_login);
				
				initView();
				verificaPermissao();
				
				carregaAnimacao();
				
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				
				session = new SessionManager(getApplicationContext());
				submit.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
								validarAcesso(edtMatricula, edtSenha);
						}
				});
		}
		
		private void carregaAnimacao() {
				Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);
				animTranslate.setAnimationListener(new Animation.AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation arg0) {
						}
						
						@Override
						public void onAnimationRepeat(Animation arg0) {
						}
						
						@Override
						public void onAnimationEnd(Animation arg0) {
								loginBox.setVisibility(View.VISIBLE);
								submit.setVisibility(View.VISIBLE);
								Animation animFade = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade);
								loginBox.startAnimation(animFade);
								submit.startAnimation(animFade);
								
						}
				});
				imgLogo.startAnimation(animTranslate);
		}
		
		private void initView() {
				loginBox = findViewById(R.id.LoginBox);
				submit = findViewById(R.id.submit);
				loginBox.setVisibility(View.GONE);
				submit.setVisibility(View.GONE);
				imgLogo = findViewById(R.id.logo);
				edtMatricula = findViewById(R.id.edtUser);
				edtSenha = findViewById(R.id.edtSenha);
		}
		
		private void verificaPermissao() {
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
										checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
								if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
										// Caso o usuário tenha negado a permissão anteriormente, e não tenha marcado o check "nunca mais mostre este alerta"
										// Podemos mostrar um alerta explicando para o usuário porque a permissão é importante.
								} else {
										// Solicita a permissao
										ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS, REQUEST_STORAGE);
								}
						}
				} else {
						//ver  o que fazer aqui
				}
		}
		
		@Override
		public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
				
				if (requestCode == REQUEST_STORAGE) {
						if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
								Toast.makeText(this, "Permissão para acessar Arquivo de Dados concedida", Toast.LENGTH_LONG).show();
						
						} else {
								super.onRequestPermissionsResult(requestCode, permissions, grantResults);
						}
				}
				
		}
		
		private void validarAcesso(final EditText edtMatricula, final EditText edtSenha) {
				// tenho que chamar a api/rest e validar se der tudo certo crio a sessao do usuario
				
				final ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setTitle("Login");
				progressDialog.setMessage("Realizando Login...");
				progressDialog.show();
			
				LoginService loginService = new RetrofitConfig().getLoginService();
				
				Call<Funcionario> autenticaLoginCall = loginService.autenticaLogin(edtSenha.getText().toString(), Long.parseLong(edtMatricula.getText().toString()));
				
				autenticaLoginCall.enqueue(new Callback<Funcionario>() {
						@Override
						public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
								
								    Funcionario funcionario = response.body();
								    if(funcionario.getId()==Long.parseLong(edtMatricula.getText().toString()))
										session.createUserLoginSession(edtMatricula.getText().toString(), edtSenha.getText().toString(),funcionario.getNome());
										Intent i = new Intent(getApplicationContext(), ClienteActivity.class);
										i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										progressDialog.dismiss();
										startActivity(i);
										finish();
						}
						
						
						@Override
						public void onFailure(Call<Funcionario> call, Throwable t) {
								
								
								progressDialog.dismiss();
								Log.e("LoginService   ", "Erro ao autenticar:" + t.getMessage() );
								
						}
				});
				
				
		}
		
		
}

		

