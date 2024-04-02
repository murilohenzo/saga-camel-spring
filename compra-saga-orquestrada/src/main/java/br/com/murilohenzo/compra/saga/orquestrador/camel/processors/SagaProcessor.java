package br.com.murilohenzo.compra.saga.orquestrador.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 * Processador da Saga.
 * Este processador é responsável por manipular as mensagens de entrada antes de iniciar uma etapa da saga.
 */
@Component
public class SagaProcessor implements Processor {

    /**
     * Processa a mensagem de entrada antes de iniciar uma etapa da saga.
     * Este método recupera os IDs da transação e do pedido dos cabeçalhos da mensagem de entrada
     * e os define novamente nos cabeçalhos da mensagem de entrada.
     *
     * @param exchange O objeto Exchange que contém a mensagem de entrada.
     * @throws Exception Se ocorrer algum erro durante o processamento da mensagem.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        // Recupera os IDs da transação e do pedido dos cabeçalhos da mensagem de entrada
        Long id = exchange.getMessage().getHeader("id", Long.class);
        Long orderId = exchange.getMessage().getHeader("orderId", Long.class);

        // Define os IDs novamente nos cabeçalhos da mensagem de entrada
        exchange.getIn().setHeader("id", id);
        exchange.getIn().setHeader("orderId", orderId);
    }
}
