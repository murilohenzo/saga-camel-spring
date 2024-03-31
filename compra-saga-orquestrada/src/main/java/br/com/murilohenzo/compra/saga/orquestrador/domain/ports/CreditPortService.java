package br.com.murilohenzo.compra.saga.orquestrador.domain.ports;

import org.apache.camel.Header;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "creditService", url = "${apis.ms-credits}/credits")
public interface CreditPortService {

    @PostMapping("/orders/{orderId}")
    void newOrderValue(@PathVariable("orderId") @Header("orderId") Long orderId, @RequestParam("value") @Header("value") int value);

    @PostMapping("/cancel")
    void cancelOrderValue();

}
