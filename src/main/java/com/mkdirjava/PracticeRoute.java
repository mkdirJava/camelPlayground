package com.mkdirjava;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;

public class PracticeRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:PracticeRoute")
                .choice()
                .when((exchange)-> exchange.getMessage().getBody(String.class) == "HI")
                .log("I got HI")
                .to("{{PracticeRouteEnd.hiEnd}}")
                .bean("ACTION","action")
                .when((exchange)-> exchange.getMessage().getBody(String.class) == "HOME")
                .log("I got HOME")
                .to("{{PracticeRouteEnd.homeEnd}}")
                .otherwise()
                .log("OTHERWISE")
                .to("{{PracticeRouteEnd.otherwise}}")
                .endChoice();
    }
}
