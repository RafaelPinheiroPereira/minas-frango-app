package com.br.minasfrango.util;

import com.br.minasfrango.service.LoginService;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
		
		private Retrofit retrofit;
		
		public RetrofitConfig() {
				this.retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/rest/minasFrango/").
								addConverterFactory(JacksonConverterFactory.create()).
								build();
		}
		
		public LoginService getLoginService() {
				return this.retrofit.create(LoginService.class);
		}
}
