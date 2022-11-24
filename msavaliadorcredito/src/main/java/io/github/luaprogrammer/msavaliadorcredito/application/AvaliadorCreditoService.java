package io.github.luaprogrammer.msavaliadorcredito.application;

import feign.FeignException;
import io.github.luaprogrammer.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.luaprogrammer.msavaliadorcredito.application.ex.ErroComunicacaoMicroserviceException;
import io.github.luaprogrammer.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.luaprogrammer.msavaliadorcredito.domain.model.DadosCliente;
import io.github.luaprogrammer.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.luaprogrammer.msavaliadorcredito.infra.CartaoResourceClient;
import io.github.luaprogrammer.msavaliadorcredito.infra.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient client;
    private final CartaoResourceClient cartao;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroserviceException {

        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = client.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartao.getCartoesByCliente(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroserviceException(e.getMessage(), status);
        }
    }

}
