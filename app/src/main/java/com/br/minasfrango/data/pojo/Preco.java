package com.br.minasfrango.data.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data

@EqualsAndHashCode(callSuper = false)
public class Preco extends RealmObject implements Serializable {
		
		@PrimaryKey
		private String id;
		PrecoID chavesPreco;
		private double valor;

	@Override
	public String toString() {
		return "Preco{" +
				"id='" + id + '\'' +
				", chavesPreco=" + chavesPreco +
				", valor=" + valor +
				'}';
	}
}
