package com.br.minasfrango.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Funcionario extends RealmObject implements Serializable {
		
		@PrimaryKey
		private long id;
		
		private String senha;
		
		private String nome;
		
		private String tipoFuncionario;
		
		
		
		
}
