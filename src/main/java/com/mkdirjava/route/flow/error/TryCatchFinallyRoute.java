package com.mkdirjava.route.flow.error;

import com.mkdirjava.exception.ExceptionOne;
import org.apache.camel.builder.RouteBuilder;

public class TryCatchFinallyRoute extends RouteBuilder {

    static final String UNHANDLED_EXCEPTION = "DO_AN_ERROR";

    @Override
    public void configure() {

        // This is the route handler for situations where an Exception is thrown.
        // In a try and catch expression all exceptions must be handled in the catch
        onException(Exception.class)
                .to("{{deadLetterEndpoint}}");

        from("direct:tryCatchRoute")
                .doTry()
                    .choice()
                        .when(exchange -> {
                            var shouldHandle = exchange.getMessage().getBody(String.class) == UNHANDLED_EXCEPTION;
                            if (shouldHandle) {
                                System.out.println("I am going to throw an exception " + exchange.getExchangeId());
                            } else {
                                System.out.println("I am not going to throw an exception ");
                            }
                            return shouldHandle;
                        })
                        .throwException(new Exception(UNHANDLED_EXCEPTION))
                        .otherwise()
                            .bean("throwExceptionOneBean")
                    .endChoice()
                .endDoTry()
                .doCatch(ExceptionOne.class)
                    .bean("errorCatchingBean")
                .doFinally()
                    .bean("finalBean")
                .to("{{successEndpoint}}");
    }
}
