package br.com.murilohenzo.compra.saga.orquestrador.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class CancelOrderProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
// TODO: analisar problema do contexto da exchange nao estar trazendo o id para realizar o undo
//        Long id = exchange.getIn().getBody(Long.class);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");
//        exchange.getIn().setHeader("id", id);
    }
}
