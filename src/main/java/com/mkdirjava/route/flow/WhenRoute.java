package com.mkdirjava.route.flow;

import org.apache.camel.builder.RouteBuilder;

public class WhenRoute extends RouteBuilder {

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
