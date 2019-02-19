package com.br.minasfrango.model.com.br.minasfrango.dto;

import com.br.minasfrango.model.Recebimento;

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
		public static Recebimento transformaDTOParaModel(RecebimentoDTO recebimentoDTO){
				return new Recebimento(recebimentoDTO.getIdFuncionario(),recebimentoDTO.getIdCliente(),recebimentoDTO.getIdVenda(),recebimentoDTO.getDataVenda(),recebimentoDTO.getValorVenda());
		}
}
