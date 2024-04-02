package br.com.murilohenzo.compra.saga.orquestrador.domain.ports;

import org.apache.camel.Header;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Porta de Serviço para Interação com o Serviço de Pedidos.
 * Esta interface define os métodos para interagir com o serviço de pedidos através do FeignClient.
 */
@FeignClient(value = "orderService", url = "${apis.ms-orders}/orders")
public interface OrderPortService {

    /**
     * Cria um novo pedido no serviço de pedidos.
     *
     * @param id O ID do pedido.
     */
    @PostMapping("/{id}")
    void newOrder(@PathVariable("id") @Header("id") Long id);

    /**
     * Cancela um pedido no serviço de pedidos.
     *
     * @param id O ID do pedido.
     */
    @PostMapping("/{id}/cancel")
    Object cancelOrder(@PathVariable("id") @Header("id") Long id);

}
