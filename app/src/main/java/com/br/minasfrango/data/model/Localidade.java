package com.br.minasfrango.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class Localidade extends RealmObject implements Serializable {
		
		@PrimaryKey
		private long id;
		
		private String nome;
		
		private Rota rota;
		
}
