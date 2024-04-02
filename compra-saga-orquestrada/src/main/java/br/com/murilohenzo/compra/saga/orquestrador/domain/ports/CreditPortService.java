package br.com.murilohenzo.compra.saga.orquestrador.domain.ports;

import org.apache.camel.Header;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Porta de Serviço para Interação com o Serviço de Crédito.
 * Esta interface define os métodos para interagir com o serviço de crédito através do FeignClient.
 */
@FeignClient(value = "creditService", url = "${apis.ms-credits}/credits")
public interface CreditPortService {

    /**
     * Cria um novo pedido de valor no serviço de crédito.
     *
     * @param orderId O ID do pedido.
     * @param value   O valor do pedido.
     */
    @PostMapping("/orders/{orderId}")
    void newOrderValue(@PathVariable("orderId") @Header("orderId") Long orderId, @RequestParam("value") @Header("value") int value);

    /**
     * Cancela um pedido de valor no serviço de crédito.
     */
    @PostMapping("/cancel")
    void cancelOrderValue();

}
