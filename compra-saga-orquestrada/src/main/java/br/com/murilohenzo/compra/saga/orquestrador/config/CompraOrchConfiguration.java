package br.com.murilohenzo.compra.saga.orquestrador.config;

import br.com.murilohenzo.compra.saga.orquestrador.camel.processors.SagaProcessor;
import br.com.murilohenzo.compra.saga.orquestrador.camel.routes.CompraSagaRouter;
import br.com.murilohenzo.compra.saga.orquestrador.domain.ports.CreditPortService;
import br.com.murilohenzo.compra.saga.orquestrador.domain.ports.OrderPortService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuração da Orquestração da Saga de Compra.
 * Esta classe configura e inicializa os componentes necessários para a orquestração da saga de compra.
 */
@Slf4j
@Configuration
public class CompraOrchConfiguration {

  /**
   * Bean para inicialização do roteador da saga de compra.
   *
   * @param sagaProcessor     O processador da saga.
   * @param orderPortService  O serviço de porta de pedidos.
   * @param creditPortService O serviço de porta de crédito.
   * @return O roteador da saga de compra.
   */
  @Bean
  public CompraSagaRouter compraSagaRouter(SagaProcessor sagaProcessor,
                                        OrderPortService orderPortService, CreditPortService creditPortService) {
    log.info("[I30] - Inicializando Saga Router");
    return new CompraSagaRouter(sagaProcessor,
            orderPortService, creditPortService);
  }
}
