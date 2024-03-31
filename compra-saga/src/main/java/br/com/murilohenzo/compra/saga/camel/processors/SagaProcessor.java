package br.com.murilohenzo.compra.saga.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SagaProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    var inboundMessage = exchange.getIn();
    log.info("<< SimpleProcessor - InboudMessage: {}", inboundMessage);
  }
  
}
