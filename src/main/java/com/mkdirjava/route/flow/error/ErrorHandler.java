package com.mkdirjava.route.flow.error;

import org.apache.camel.builder.RouteBuilder;

public class ErrorHandler extends RouteBuilder {
        @Override
        public void configure() {

            errorHandler(
                    deadLetterChannel("direct:globalDeadLetter")
                            .maximumRedeliveries(3)
            );

            from("direct:globalDeadLetter")
                    .bean("globalErrorHandlerBean");

            from("direct:globalError")
                    .bean("throwBean")
                    .to("{{successEndpoint}}");

            // Above uses the global error Handler
            // Below uses a route specific Handler

            from("direct:routeError")
                    .errorHandler(deadLetterChannel("direct:routeDeadLetter"))
                    .bean("throwBean")
                    .to("{{successEndpoint}}");

            from("direct:routeDeadLetter")
                    .bean("routeErrorHandlerBean");
        }

}
