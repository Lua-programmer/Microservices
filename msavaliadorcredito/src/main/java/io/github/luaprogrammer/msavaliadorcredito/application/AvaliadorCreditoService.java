package io.github.luaprogrammer.msavaliadorcredito.application;

import feign.FeignException;
import io.github.luaprogrammer.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.luaprogrammer.msavaliadorcredito.application.ex.ErroComunicacaoMicroserviceException;
import io.github.luaprogrammer.msavaliadorcredito.application.ex.ErroSolicitacaoCartaoException;
import io.github.luaprogrammer.msavaliadorcredito.domain.model.*;
import io.github.luaprogrammer.msavaliadorcredito.infra.clients.CartaoResourceClient;
import io.github.luaprogrammer.msavaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.luaprogrammer.msavaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient client;
    private final CartaoResourceClient cartao;

    private final SolicitacaoEmissaoCartaoPublisher publisher;

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

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroserviceException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = client.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartao.getCartoesRendaAte(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var lisCartoesAprovados = cartoes.stream().map(c -> {

                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = c.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                BigDecimal fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(c.getNome());
                aprovado.setBandeira(c.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(lisCartoesAprovados);

        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroserviceException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados) {
        try {
            publisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }

}
