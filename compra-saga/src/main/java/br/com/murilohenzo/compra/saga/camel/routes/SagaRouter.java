package br.com.murilohenzo.compra.saga.camel.routes;

import br.com.murilohenzo.compra.saga.camel.processors.SagaProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.saga.CamelSagaService;
import org.apache.camel.saga.InMemorySagaService;
import org.springframework.beans.factory.annotation.Value;

import static br.com.murilohenzo.compra.saga.camel.consts.RouterOperations.*;

@RequiredArgsConstructor
public class SagaRouter extends RouteBuilder {

  private final SagaProcessor sagaProcessor;

  @Value("${apis.ms-orders}")
  private String ORDERS_URI;

  @Value("${apis.ms-credits}")
  private String CREDIT_URI;


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
    from(SAGA).saga().propagation(SagaPropagation.REQUIRES_NEW).log("<< INICIANDO TRANSACAO >>")
            .to(NEW_ORDER).log("<< Pedido ${header.id} criado. Saga ${body}. >>")
            .to(NEW_ORDER_VALUE).log("Credito do pedido ${header.id} no valor de BRL ${header.valor} reservado para a saga ${body}")
            .to(FINISH).log("Feito!");

    //MS-ORDER
    from(NEW_ORDER).saga().propagation(SagaPropagation.MANDATORY)
            .compensation(CANCEL_ORDER)
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .routeId("newOrderRoute")
            .process(exchange -> {
              // Obtém o ID do pedido dos cabeçalhos
              Long id = exchange.getIn().getHeader("id", Long.class);
              // Define o ID do pedido no cabeçalho para ser usado na chamada de compensação
              exchange.getIn().setHeader("id", id);
            })
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
            .toD(ORDERS_URI + "/orders/${header.id}")
            .log("Criando novo pedido com id ${header.id}");

    from(CANCEL_ORDER)
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .routeId("cancelOrderRoute")
            .process(exchange -> {
              // Obtém o ID do pedido dos cabeçalhos
              Long id = exchange.getIn().getHeader("id", Long.class);

              // Aqui você executa a lógica de compensação usando o ID do pedido
              // Por exemplo, fazer uma chamada para cancelar o pedido com o ID correto
              // Ou executar outras operações de compensação necessárias

              // Exemplo de chamada para cancelar o pedido
              String cancelOrderURI = ORDERS_URI + "/orders/" + id + "/cancel";
              exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");
              exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
              exchange.getIn().setHeader(Exchange.HTTP_URI, cancelOrderURI);

              // Defina qualquer outro cabeçalho necessário para a chamada

              // Você também pode registrar informações relevantes sobre a compensação
              exchange.getIn().setHeader("compensation", "true");
            })
//            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
//            .log("<< CANCEL_ORDER - id: ${header.id} >>")
//            .toD(ORDERS_URI + "/orders/${header.id}/cancel")
            .log("<< CANCEL_ORDER - id: ${header.id} >>")
            .log("Pedido ${body} compensado");

    //MS-CREDIT
    from(NEW_ORDER_VALUE).saga().propagation(SagaPropagation.MANDATORY)
//            .compensation(CANCEL_ORDER_VALUE)
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .log("<< NEW_ORDER_VALUE - orderId: ${header.orderId} com valor BRL: ${header.value} >>")
            .toD(CREDIT_URI + "/credits/orders/${header.orderId}?value=${header.value}")
            .log("Reservando o crédito");

    from(CANCEL_ORDER_VALUE)
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .log("<< CANCEL_ORDER_VALUE - id: ${header.id} >>")
//            .toD(CREDIT_URI + "/credits/${header.id}/cancel")
            .toD(CREDIT_URI + "/credits/cancel")
            .log("Credito compensado para a saga ${body}");
    
    //Finaliza
    from(FINISH).saga().propagation(SagaPropagation.MANDATORY)
            .choice()
            .end();
  }
  
}