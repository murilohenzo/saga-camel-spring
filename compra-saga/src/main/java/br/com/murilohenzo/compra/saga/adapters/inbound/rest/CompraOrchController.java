package br.com.murilohenzo.compra.saga.adapters.inbound.rest;

import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.murilohenzo.compra.saga.camel.consts.RouterOperations.SAGA;

@RestController
@RequestMapping("compra-orch")
@RequiredArgsConstructor
public class CompraOrchController {

    private  final CamelContext context;

    @GetMapping
    public ResponseEntity<?> saga() {
        Long id = 0L;
        buying(++id, 20);
        buying(++id, 30);
        buying(++id, 30);
        buying(++id, 50);

        return ResponseEntity.ok().build();
    }

    private void buying(Long id, int value) {
        context.createFluentProducerTemplate()
                .to(SAGA)
                .withHeader("id", id)
                .withHeader("orderId", id)
                .withHeader("value", value)
                .request();
    }
}
