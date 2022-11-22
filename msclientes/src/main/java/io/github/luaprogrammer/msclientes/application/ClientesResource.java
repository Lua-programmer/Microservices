package io.github.luaprogrammer.msclientes.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clientes")
public class ClientesResource {

    @GetMapping
    public String status() {
        return "ok";
    }
}
