package br.com.murilohenzo.order.service.adapters.inbound.rest;

import br.com.murilohenzo.order.service.domain.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderServiceController {

  private final OrderService orderService;

  @GetMapping
  public ResponseEntity<List<Long>> findAllOrders() {
    return ResponseEntity.ok(orderService.findAllOrders());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Long> findOrderById(@PathVariable("id") Long id) {
    return orderService.findOrderById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @PostMapping("/{id}")
  public ResponseEntity<Void> newOrder(@PathVariable("id") Long id) {
    orderService.newOrder(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/cancel")
  public ResponseEntity<Void> cancelOrder(@PathVariable("id") Long id) {
    orderService.cancelOrder(id);
    return ResponseEntity.ok().build();
  }

}
