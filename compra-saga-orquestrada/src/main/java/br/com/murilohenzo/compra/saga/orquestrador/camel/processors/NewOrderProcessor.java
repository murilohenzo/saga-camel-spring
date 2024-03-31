package br.com.murilohenzo.compra.saga.orquestrador.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class NewOrderProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
//        Long id = exchange.getIn().getHeader("id", Long.class);
//        exchange.getIn().setBody(id);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");
    }
}
