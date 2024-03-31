package br.com.murilohenzo.credit.service.adapters.inbound.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.murilohenzo.credit.service.domain.services.CreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("credits")
@RequiredArgsConstructor
public class CreditServiceController {

  private final CreditService creditService;

  @PostMapping("/orders/{orderId}")
  public ResponseEntity<?> newOrderValue(@PathVariable("orderId") Long orderId, @RequestParam("value") int value) {
    try{
      creditService.newOrderValue(orderId, value);
      return ResponseEntity.ok().build();
    } catch(IllegalStateException e){
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
  }

//  @PostMapping("/{id}/cancel")
  @PostMapping("/cancel")
  public ResponseEntity<Void> cancelOrderValue() {
    Long id = 0L;
    creditService.cancelOrderValue(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("totalCredit")
  public ResponseEntity<?> getTotalCredit() {
    return ResponseEntity.status(200).body(creditService.getTotalCredit());
  }

}
