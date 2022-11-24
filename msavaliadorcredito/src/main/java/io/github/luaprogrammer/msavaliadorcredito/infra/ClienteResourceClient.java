package io.github.luaprogrammer.msavaliadorcredito.infra;

import io.github.luaprogrammer.msavaliadorcredito.domain.model.DadosCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//url = "http://localhost:8080"
@FeignClient(value = "msclientes", path = "/clientes") //é um cliente http
public interface ClienteResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<DadosCliente> dadosCliente(@RequestParam("cpf") String cpf);
}
