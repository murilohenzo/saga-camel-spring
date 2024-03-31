package br.com.murilohenzo.compra.saga.config;

import br.com.murilohenzo.compra.saga.camel.routes.RestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.murilohenzo.compra.saga.camel.processors.SagaProcessor;
import br.com.murilohenzo.compra.saga.camel.routes.SagaRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

@Slf4j
@Configuration
public class CamelSagaOrch {

  @Bean
  @Order(1)
  public RestConfiguration restConfiguration() {
    log.info("[I17] - RestConfigurationRouter");
    return new RestConfiguration();
  }

  @Bean
  public SagaRouter orderRouter(SagaProcessor sagaProcessor) {
    log.info("[I16] - OrderRouter");
    return new SagaRouter(sagaProcessor);
  }
  
}
