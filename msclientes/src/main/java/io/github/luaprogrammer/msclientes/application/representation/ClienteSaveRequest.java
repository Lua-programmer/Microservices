package io.github.luaprogrammer.msclientes.application.representation;

import io.github.luaprogrammer.msclientes.domain.Cliente;
import lombok.Data;

@Data
public class ClienteSaveRequest {
    private String nome;
    private String cpf;
    private Integer idade;

    public Cliente toModel() {
        return new Cliente(nome, cpf, idade);
    }
}
