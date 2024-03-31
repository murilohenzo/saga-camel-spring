package br.com.murilohenzo.compra.saga.orquestrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BuySagaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuySagaApplication.class, args);
	}

}
