package br.com.murilohenzo.order.service.domain.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final Set<Long> orders = new HashSet<>();

  // do: create transaction
  public void newOrder(Long id) {
    orders.add(id);
  }

  // undo: compensation transaction
  public void cancelOrder(Long id) {
    orders.remove(id);
  }

  public List<Long> findAllOrders() {
    return orders.stream().toList();
  }

  public Optional<Long> findOrderById(Long id) {
    return orders.stream().filter(order -> order.equals(id)).findFirst();
  }
}
