package io.github.luaprogrammer.mscartoes.representation;

import io.github.luaprogrammer.mscartoes.domain.Cartao;
import io.github.luaprogrammer.mscartoes.enums.BandeiraCartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoSaveRequest {
    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel() {
        return new Cartao(nome, bandeira, renda, limite);
    }
}
