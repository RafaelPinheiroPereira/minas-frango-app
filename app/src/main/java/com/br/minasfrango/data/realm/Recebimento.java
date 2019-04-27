package com.br.minasfrango.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
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
		private Date dataVencimento;

		private boolean check=false;

	@Ignore
	private int orderSelected = 0;
		
		public Recebimento(long idFuncionario, long idCliente, long idVenda, Date dataVenda, double valorVenda,Date dataVencimento) {
				this.idFuncionario = idFuncionario;
				this.idCliente = idCliente;
				this.idVenda = idVenda;
				this.dataVenda = dataVenda;
				this.valorVenda = valorVenda;
				this.dataVencimento=dataVencimento;
				
		}
}
