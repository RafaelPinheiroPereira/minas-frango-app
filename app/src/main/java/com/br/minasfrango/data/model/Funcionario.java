package com.br.minasfrango.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario extends RealmObject implements Serializable {
		
		@PrimaryKey
		private long id;
		
		private String senha;
		
		private String nome;
		
		private String tipoFuncionario;
		
		
		
		
}
