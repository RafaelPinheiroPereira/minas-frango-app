package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.Cliente;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClienteORM extends RealmObject implements Serializable {

    private String bairro;

    private String cep;

    private String cidade;

    private String cpf;

    private String endereco;



    @PrimaryKey private long id;

    private LocalidadeORM mLocalidadeORM;

    private String nome;

    private String numero;

    private String razaoSocial;

    private String referencia;

    private String telefone;
    private long codigoClienteGrupo;
    private long idEmpresa;

    public ClienteORM(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.razaoSocial = cliente.getRazaoSocial();
        this.mLocalidadeORM = new LocalidadeORM(cliente.getLocalidade());
        this.cep = cliente.getCep();
        this.cpf = cliente.getCpf();
        this.bairro = cliente.getBairro();
        this.endereco = cliente.getEndereco();
        this.cidade = cliente.getCidade();
        this.referencia = cliente.getReferencia();
        this.telefone = cliente.getTelefone();
        this.numero = cliente.getNumero();
        this.codigoClienteGrupo = cliente.getCodigoClienteGrupo();
        this.idEmpresa=cliente.getIdEmpresa();
    }
}
