package br.com.murilohenzo.compra.saga.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class RestConfiguration extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .scheme("http")
                .host("localhost")
                .component("servlet")
                .bindingMode(RestBindingMode.auto)
                .clientRequestValidation(false);
    }
}