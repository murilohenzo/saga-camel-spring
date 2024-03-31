package br.com.murilohenzo.compra.saga.orquestrador.domain.ports;

import org.apache.camel.Header;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(value = "orderService", url = "${apis.ms-orders}/orders")
public interface OrderPortService {

    @PostMapping("/{id}")
    void newOrder(@PathVariable("id") @Header("id") Long id);

    @PostMapping("/{id}/cancel")
    void cancelOrder(@PathVariable("id") @Header("id") Long id);

}
