package br.com.murilohenzo.compra.saga.orquestrador.config;

import br.com.murilohenzo.compra.saga.camel.routes.RestConfiguration;
import br.com.murilohenzo.compra.saga.orquestrador.camel.processors.CancelOrderProcessor;
import br.com.murilohenzo.compra.saga.orquestrador.camel.processors.NewOrderProcessor;
import br.com.murilohenzo.compra.saga.orquestrador.camel.routes.BuySagaRouter;
import br.com.murilohenzo.compra.saga.orquestrador.domain.ports.CreditPortService;
import br.com.murilohenzo.compra.saga.orquestrador.domain.ports.OrderPortService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

@Slf4j
@Configuration
public class BuySagaOrchestrationConfiguration {

  @Bean
  @Order(1)
  public RestConfiguration restConfiguration() {
    log.info("[I17] - RestConfigurationRouter");
    return new RestConfiguration();
  }

  @Bean
  public BuySagaRouter buySagaRouter(NewOrderProcessor newOrderProcessor, CancelOrderProcessor cancelOrderProcessor,
                                     OrderPortService orderPortService, CreditPortService creditPortService) {
    log.info("[I16] - OrderRouter");
    return new BuySagaRouter(newOrderProcessor, cancelOrderProcessor,
            orderPortService, creditPortService);
  }
  
}
