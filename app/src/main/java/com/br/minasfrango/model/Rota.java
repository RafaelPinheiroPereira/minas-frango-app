package com.br.minasfrango.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=false,includeFieldNames=false)
public class Rota  extends RealmObject implements Serializable {
		
		@PrimaryKey
		@ToString.Exclude private long id;
		
		@ToString.Exclude private Funcionario funcionario;
		
		private String nome;
		
		@Override public String toString() {
				return this.getNome() ;
		}
		
}
