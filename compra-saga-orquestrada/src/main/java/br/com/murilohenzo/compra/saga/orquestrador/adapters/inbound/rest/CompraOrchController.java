package br.com.murilohenzo.compra.saga.orquestrador.adapters.inbound.rest;

import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.murilohenzo.compra.saga.orquestrador.camel.consts.RouterOperations.SAGA;


@RestController
@RequestMapping("compra-orch")
@RequiredArgsConstructor
public class CompraOrchController {

    private  final CamelContext context;

    @PostMapping
    public ResponseEntity<?> saga() {
        try {
            Long id = 0L;
            buying(++id, 20);
            buying(++id, 30);
            buying(++id, 30);
            buying(++id, 50);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private void buying(Long id, int value) {
        try {
            context.createFluentProducerTemplate()
                    .to(SAGA)
                    .withHeader("id", id)
                    .withHeader("orderId", id)
                    .withHeader("value", value)
                    .request();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
