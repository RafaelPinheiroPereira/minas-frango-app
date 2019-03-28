package com.br.minasfrango.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.Data;

@Data


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
