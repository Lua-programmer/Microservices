package io.github.luaprogrammer.msavaliadorcredito.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queues.emissao-cartoes}")
    private String emissaoCartoesFila;

    public Queue queueemissaoCartoes(){
        return new Queue( emissaoCartoesFila,true);
    }
}
