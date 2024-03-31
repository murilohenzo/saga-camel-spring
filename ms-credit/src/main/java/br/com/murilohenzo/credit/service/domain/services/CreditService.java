package br.com.murilohenzo.credit.service.domain.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CreditService {

  private int totalCredit;
  private final Map<Long, Integer> valueOrders = new HashMap<>();

  public CreditService() {
    this.totalCredit = 100;
  }

  // do: create transaction
  public void newOrderValue(Long orderId, int value) {
    if (value > totalCredit) {
      throw new IllegalStateException("Saldo insuficiente");
    }

    totalCredit = totalCredit - value;
    valueOrders.put(orderId, value);
  }

  // undo: compensation transaction
  public void cancelOrderValue(Long orderId) {
    System.out.println("PedidoValor falhou. Iniciando cancelamento do pedido.");
//    totalCredit = totalCredit + valueOrders.get(orderId);
//    valueOrders.remove(orderId);
  }

  public int getTotalCredit() {
    return totalCredit;
  }
  
}
