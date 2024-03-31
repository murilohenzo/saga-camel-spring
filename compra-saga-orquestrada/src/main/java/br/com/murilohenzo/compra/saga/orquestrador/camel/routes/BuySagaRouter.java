package br.com.murilohenzo.compra.saga.orquestrador.camel.routes;

import br.com.murilohenzo.compra.saga.orquestrador.camel.processors.CancelOrderProcessor;
import br.com.murilohenzo.compra.saga.orquestrador.camel.processors.NewOrderProcessor;
import br.com.murilohenzo.compra.saga.orquestrador.domain.ports.CreditPortService;
import br.com.murilohenzo.compra.saga.orquestrador.domain.ports.OrderPortService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.saga.CamelSagaService;
import org.apache.camel.saga.InMemorySagaService;

import static br.com.murilohenzo.compra.saga.orquestrador.camel.consts.RouterOperations.*;


@RequiredArgsConstructor
public class BuySagaRouter extends RouteBuilder {

  private final NewOrderProcessor newOrderProcessor;

  private final CancelOrderProcessor cancelOrderProcessor;

  private final OrderPortService orderPortService;

  private final CreditPortService creditPortService;


  @Override
  public void configure() throws Exception {

    CamelSagaService sagaService = new InMemorySagaService();
    getContext().addService(sagaService);
    getContext().setMessageHistory(true);
    getContext().setSourceLocationEnabled(true);

//    rest()
//            .post("/orders/{id}")
//            .description("Add a new order")
//            .id("addOrderApi")
//            .produces("application/json")
//            .consumes("application/json")
//            .param()
//            .name("id")
//            .type(RestParamType.path)
//            .required(true)
//            .description("Order id")
//            .endParam()
//            .to(NEW_ORDER);
//
//    rest()
//            .post("/orders/{id}/cancel")
//            .description("Cancel an existing order")
//            .id("cancelOrderApi")
//            .produces("application/json")
//            .consumes("application/json")
//            .param()
//            .name("id")
//            .type(RestParamType.path)
//            .required(true)
//            .description("Order id")
//            .endParam()
//            .to(CANCEL_ORDER);


    //SAGA
    from(SAGA)
            .saga()
            .propagation(SagaPropagation.REQUIRES_NEW)
            .log("<< INICIANDO TRANSACAO >>")
            .to(NEW_ORDER)
            .log("<< Pedido ${header.id} criado. Saga ${body}. >>")
            .to(NEW_ORDER_VALUE)
            .log("Credito do pedido ${header.id} no valor de BRL ${header.value} reservado para a saga ${body}")
            .to(FINISH).log("Feito!");

    //MS-ORDER
    from(NEW_ORDER)
            .routeId("newOrderRoute")
            .process(newOrderProcessor)
            .saga()
              .propagation(SagaPropagation.MANDATORY)
              .compensation(CANCEL_ORDER)
              .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .log("<< INITIALIZE NEW_ORDER >>")
            .bean(orderPortService, "newOrder")
            .log("Criando novo pedido com id #${header.id}");

    from(CANCEL_ORDER)
            .routeId("cancelOrderRoute")
            .process(cancelOrderProcessor)
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .log("<< INITIALIZE CANCEL_ORDER >>")
            .bean(orderPortService, "cancelOrder")
            .log("Pedido ${body} compensado");

    //MS-CREDIT
    from(NEW_ORDER_VALUE).saga().propagation(SagaPropagation.MANDATORY)
            .compensation(CANCEL_ORDER_VALUE)
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .log("<< INITIALIZE NEW_ORDER_VALUE >>")
            .routeId("newOrderValueRoute")
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .bean(creditPortService, "newOrderValue")
            .log("Reservando o crédito");

    from(CANCEL_ORDER_VALUE)
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .log("<< INITIALIZE CANCEL_ORDER_VALUE >>")
            .routeId("cancelOrderValueRoute")
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .bean(creditPortService, "cancelOrderValue")
            .log("Credito compensado para a saga ${body}");
    
    //Finaliza
    from(FINISH).saga().propagation(SagaPropagation.MANDATORY)
            .choice()
            .end();
  }
  
}