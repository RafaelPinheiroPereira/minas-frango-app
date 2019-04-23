package com.br.minasfrango.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)


public class Rota  extends RealmObject implements Serializable {
		
		@PrimaryKey
		@ToString.Exclude private long id;
		
		@ToString.Exclude private Funcionario funcionario;
		
		private String nome;
		
		@Override public String toString() {
				return this.getNome() ;
		}
		
}
