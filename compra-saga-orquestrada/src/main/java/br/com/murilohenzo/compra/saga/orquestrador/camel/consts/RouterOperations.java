package br.com.murilohenzo.compra.saga.orquestrador.camel.consts;

public class RouterOperations {

    private RouterOperations() {}

    public static final String NEW_ORDER = "direct:newOrder";

    public static final String CANCEL_ORDER = "direct:cancelOrder";

    public static final String NEW_ORDER_VALUE = "direct:newOrderValue";

    public static final String CANCEL_ORDER_VALUE = "direct:cancelOrderValue";

    public static final String SAGA = "direct:saga";

    public static final String FINISH = "direct:finish";
}
