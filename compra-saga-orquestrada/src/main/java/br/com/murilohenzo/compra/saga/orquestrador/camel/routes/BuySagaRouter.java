package br.com.murilohenzo.compra.saga.orquestrador.camel.routes;

import br.com.murilohenzo.compra.saga.orquestrador.camel.processors.SagaProcessor;
import br.com.murilohenzo.compra.saga.orquestrador.domain.ports.CreditPortService;
import br.com.murilohenzo.compra.saga.orquestrador.domain.ports.OrderPortService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.saga.CamelSagaService;
import org.apache.camel.saga.InMemorySagaService;

import static br.com.murilohenzo.compra.saga.orquestrador.camel.consts.RouterOperations.*;

/**
 * Roteador da Saga de Compra.
 * Este roteador configura a saga que coordena transações distribuídas relacionadas à compra de um pedido.
 */
@RequiredArgsConstructor
public class BuySagaRouter extends RouteBuilder {

  public static final String ID = "id";
  public static final String ORDER_ID = "orderId";
  private final SagaProcessor sagaProcessor;
  private final OrderPortService orderPortService;
  private final CreditPortService creditPortService;

  @Override
  public void configure() throws Exception {

    // Configuração do serviço de saga
    CamelSagaService sagaService = new InMemorySagaService();
    getContext().addService(sagaService);
    getContext().setMessageHistory(true);
    getContext().setSourceLocationEnabled(true);

    // Configuração da Saga de Compra
    from(SAGA)
            .process(sagaProcessor)
            .saga()
            .propagation(SagaPropagation.REQUIRES_NEW)
            .log("<< INICIANDO TRANSACAO PARA FLUXO DE COMPRA >>")
            .to(NEW_ORDER)
            .log("<< Pedido ${header.id} criado. Saga ${body}. >>")
            .to(NEW_ORDER_VALUE)
            .log("Credito do pedido ${header.id} no valor de BRL ${header.value} reservado para a saga ${body}")
            .to(FINISH).log("Feito!");

    // Roteador MS-ORDER
    from(NEW_ORDER)
            .routeId("newOrderRoute")
            .saga()
            .propagation(SagaPropagation.MANDATORY)
            .option(ID, header(ID))
            .option(ORDER_ID, header(ORDER_ID))
            .setBody(body())
            .compensation(CANCEL_ORDER)
            .log("<< INICIALIZANDO NOVO PEDIDO >>")
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .bean(orderPortService, "newOrder")
            .log("Criando novo pedido com id #${header.id}");

    // Roteador MS-CREDIT
    from(NEW_ORDER_VALUE)
            .routeId("newOrderValueRoute")
            .saga()
            .propagation(SagaPropagation.MANDATORY)
            .option(ID, header(ID))
            .option(ORDER_ID, header(ORDER_ID))
            .option("body", body())
            .compensation(CANCEL_ORDER_VALUE)
            .log("<< INICIALIZANDO NOVO_PEDIDO_VALOR >>")
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .bean(creditPortService, "newOrderValue")
            .log("Reservando o crédito");

    // Roteador de Compensação para MS-ORDER
    from(CANCEL_ORDER)
            .routeId("cancelOrderRoute")
            .log("<< INICIALIZADO CANCELAMENTO_PEDIDO >>")
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .bean(orderPortService, "cancelOrder")
            .log("Pedido #${header.id} compensado");

    // Roteador de Compensação para MS-CREDIT
    from(CANCEL_ORDER_VALUE)
            .routeId("cancelOrderValueRoute")
            .log("<< INICIALIZANDO ESTORNO DO VALOR D PEDIDO >>")
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .bean(creditPortService, "cancelOrderValue")
            .log("Credito compensado para a saga ${body}");

    // Roteador de Conclusão da Saga
    from(FINISH)
            .saga()
            .propagation(SagaPropagation.MANDATORY)
            .choice()
            .end();
  }

}
