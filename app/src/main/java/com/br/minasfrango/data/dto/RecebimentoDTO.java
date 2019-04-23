package com.br.minasfrango.data.dto;

import com.br.minasfrango.data.realm.Recebimento;
import java.io.Serializable;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecebimentoDTO implements Serializable {
		
		private long idFuncionario;
		private long idCliente;
		private long idVenda;
		private Date dataVenda;
		private double valorVenda;
		private Date dataVencimento;
		
		public static Recebimento transformaDTOParaModel(RecebimentoDTO recebimentoDTO) {
				return new Recebimento(recebimentoDTO.getIdFuncionario(),
								recebimentoDTO.getIdCliente(),
								recebimentoDTO.getIdVenda(),
								recebimentoDTO.getDataVenda(),
								recebimentoDTO.getValorVenda(),
								recebimentoDTO.getDataVencimento());
		}
}
