package io.github.luaprogrammer.msavaliadorcredito.application;

import io.github.luaprogrammer.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.luaprogrammer.msavaliadorcredito.domain.model.DadosCliente;
import io.github.luaprogrammer.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.luaprogrammer.msavaliadorcredito.infra.CartaoResourceClient;
import io.github.luaprogrammer.msavaliadorcredito.infra.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient client;
    private final CartaoResourceClient cartao;

    public SituacaoCliente obterSituacaoCliente(String cpf) {

        ResponseEntity<DadosCliente> dadosClienteResponse = client.dadosCliente(cpf);
        ResponseEntity<List<CartaoCliente>> cartoesResponse = cartao.getCartoesByCliente(cpf);

        return SituacaoCliente
                .builder()
                .cliente(dadosClienteResponse.getBody())
                .cartoes(cartoesResponse.getBody())
                .build();
    }

}
