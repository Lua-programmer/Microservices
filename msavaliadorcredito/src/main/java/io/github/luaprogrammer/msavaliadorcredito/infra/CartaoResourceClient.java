package io.github.luaprogrammer.msavaliadorcredito.infra;

import io.github.luaprogrammer.msavaliadorcredito.domain.model.Cartao;
import io.github.luaprogrammer.msavaliadorcredito.domain.model.CartaoCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//url = "http://localhost:8080"
@FeignClient(value = "mscartoes", path = "/cartoes")
public interface CartaoResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<List<CartaoCliente>> getCartoesByCliente(@RequestParam("cpf") String cpf);

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda);
}
