package com.br.minasfrango.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Recebimento extends RealmObject implements Serializable {
		
		@PrimaryKey
		private long id;
		private long idFuncionario;
		private long idCliente;
		private long idVenda;
		private Date dataVenda;
		private double valorVenda;
		private double valorAmortizado;
		private Date dataRecebimento;
		private long tipoRecebimento;
		
		public Recebimento(long idFuncionario, long idCliente, long idVenda, Date dataVenda, double valorVenda) {
				this.idFuncionario = idFuncionario;
				this.idCliente = idCliente;
				this.idVenda = idVenda;
				this.dataVenda = dataVenda;
				this.valorVenda = valorVenda;
				
		}
}
