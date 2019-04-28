package com.br.minasfrango.data.realm;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@RealmClass
@NoArgsConstructor
@AllArgsConstructor
public class Cliente implements RealmModel, Serializable {

    private String bairro;

    private String nome;
    private String razaoSocial;

    private String cep;

    private String cidade;

    private String cpf;
    private String referencia;

    private String telefone;

    private String endereco;

    @PrimaryKey
    private long id;

    private Localidade localidade;

    private String numero;

    public static Cliente convertRealmToDTO(Cliente clienteToTransforme) {
        Funcionario funcionario =
                new Funcionario(
                        clienteToTransforme.getLocalidade().getRota().getFuncionario().getId(),
                        clienteToTransforme.getLocalidade().getRota().getFuncionario().getSenha(),
                        clienteToTransforme.getLocalidade().getRota().getFuncionario().getNome(),
                        clienteToTransforme
                                .getLocalidade()
                                .getRota()
                                .getFuncionario()
                                .getTipoFuncionario());
        Rota rota =
                new Rota(
                        clienteToTransforme.getLocalidade().getRota().getId(),
                        funcionario,
                        clienteToTransforme.getLocalidade().getRota().getNome());
        Localidade localidade =
                new Localidade(
                        clienteToTransforme.getLocalidade().getId(),
                        clienteToTransforme.getLocalidade().getNome(),
                        rota);
        Cliente cliente =
                new Cliente(
                        clienteToTransforme.getId(),
                        clienteToTransforme.getNome(),
                        clienteToTransforme.getRazaoSocial(),
                        localidade,
                        clienteToTransforme.getEndereco(),
                        clienteToTransforme.getNumero(),
                        clienteToTransforme.getBairro(),
                        clienteToTransforme.getCidade(),
                        clienteToTransforme.getCep(),
                        clienteToTransforme.getReferencia(),
                        clienteToTransforme.getTelefone(),
                        clienteToTransforme.getCpf());
        return cliente;
    }
}
