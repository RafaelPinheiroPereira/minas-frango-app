package com.br.minasfrango.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends RealmObject implements Serializable {
		
		@PrimaryKey
		private long id;
		
		private String nome;
		
		private String razaoSocial;
		
		private Localidade localidade;
		
		private String endereco;
		
		private String numero;
		
		private String bairro;
		
		private String cidade;
		
		private String cep;
		
		private String referencia;
		
		private String telefone;
		
		private String cpf;
		
		
		
		
}
