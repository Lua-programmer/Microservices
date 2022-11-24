package io.github.luaprogrammer.msavaliadorcredito.application;

import io.github.luaprogrammer.msavaliadorcredito.domain.model.DadosCliente;
import io.github.luaprogrammer.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.luaprogrammer.msavaliadorcredito.infra.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient client;

    public SituacaoCliente obterSituacaoCliente(String cpf) {
        //obter dados do msclientes e obter cartoes do cliente mscartoes

        ResponseEntity<DadosCliente> dadosClienteResponse = client.dadosCliente(cpf);

        return SituacaoCliente.builder().cliente(dadosClienteResponse.getBody()).build();
    }

}
