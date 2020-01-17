package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.ClienteORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Cliente implements Serializable {

    private String bairro;

    private String cep;

    private String cidade;

    private String cpf;

    private String endereco;

    private long id;

    private Localidade localidade;

    private String nome;

    private String numero;

    private String razaoSocial;

    private String referencia;

    private String telefone;

    private long idEmpresa;


    private long codigoClienteGrupo;

    public Cliente(ClienteORM clienteORM) {
        this.id = clienteORM.getId();
        this.nome = clienteORM.getNome();
        this.razaoSocial = clienteORM.getRazaoSocial();
        this.localidade = new Localidade(clienteORM.getMLocalidadeORM());
        this.cep = clienteORM.getCep();
        this.cpf = clienteORM.getCpf();
        this.bairro = clienteORM.getBairro();
        this.endereco = clienteORM.getEndereco();
        this.cidade = clienteORM.getCidade();
        this.referencia = clienteORM.getReferencia();
        this.telefone = clienteORM.getTelefone();
        this.numero = clienteORM.getNumero();
        this.codigoClienteGrupo=clienteORM.getCodigoClienteGrupo();
        this.idEmpresa=clienteORM.getIdEmpresa();

    }
}
