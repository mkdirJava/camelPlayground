package com.mkdirjava.route;

import com.mkdirjava.exception.ExceptionOne;
import org.apache.camel.builder.RouteBuilder;

public class TryCatchFinallyRoute extends RouteBuilder {
    @Override
    public void configure() {

        onException(ExceptionOne.class)
                .maximumRedeliveries(3)
                .continued(true)
                .process(exchange -> System.out.println("Processing some exception "+ exchange.getExchangeId()))
                .to("direct:ExceptionHandler");

        from("direct:ExceptionHandler")
                    .process(exchange -> {
                        System.out.println("In the exception handler "+ exchange.getExchangeId());
                    }).to("mock:deadPlace");


        from("direct:tryCatchRoute")
                .doTry()
                    .process(exchange ->{
                        System.out.println("I am in the process 1 ID:" + exchange.getExchangeId());
                        System.out.println("Error happened "+ exchange.getExchangeId());
                        throw new ExceptionOne("Something went wrong");
                    }).process(exchange -> {
                        System.out.println("I am in the process 2 ID: " + exchange.getExchangeId());
                    }).process(exchange -> {
                        System.out.println("I am in the process 3 ID:" + exchange.getExchangeId());
                    })
                .endDoTry()
                .doCatch(ExceptionOne.class)
                .process(exchange -> {
                    System.out.println("In the catch ID: "+exchange.getExchangeId());
                })
                .doFinally()
                .process(exchange -> System.out.println("In the final"));

        from("direct:globalErrorRoute")
                .doTry()
                .process(exchange ->{
                    System.out.println("I am in the process 1 ID:" + exchange.getExchangeId());
                    System.out.println("Error happened "+ exchange.getExchangeId());
                    throw new Exception("Something went wrong");
                })
                .endDoTry()
                .doCatch(ExceptionOne.class)
                .process(exchange -> {
                    System.out.println("In the catch ID: "+exchange.getExchangeId());
                })
                .doFinally()
                .process(exchange -> System.out.println("In the final"));
    }
}
