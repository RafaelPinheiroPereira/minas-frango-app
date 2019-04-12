package com.br.minasfrango.util;

 import com.br.minasfrango.service.ExportacaoService;
import com.br.minasfrango.service.ImportacaoService;
import com.br.minasfrango.service.LoginService;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
		
		private Retrofit retrofit;
		
		public RetrofitConfig() {
				this.retrofit = new Retrofit.Builder().
								baseUrl("http://10.0.2.2:8080/rest/minasFrango/").
								//baseUrl("http://192.168.0.176:8080/rest/minasFrango/").
								addConverterFactory(JacksonConverterFactory.create()).
								build();
		}
		
		public LoginService getLoginService() {
				return this.retrofit.create(LoginService.class);
		}
		
		public ImportacaoService getImportacaoService() {
				return this.retrofit.create(ImportacaoService.class);
		}
		
		public ExportacaoService getExportacaoService() {
				return this.retrofit.create(ExportacaoService.class);
		}
}
