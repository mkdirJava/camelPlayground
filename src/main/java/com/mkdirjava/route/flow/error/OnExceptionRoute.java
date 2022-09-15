package com.mkdirjava.route.flow.error;

import com.mkdirjava.exception.ExceptionOne;
import org.apache.camel.builder.RouteBuilder;

public class OnExceptionRoute extends RouteBuilder {
        @Override
        public void configure() {

            onException(ExceptionOne.class)
                    .maximumRedeliveries(3)
                    .handled(true)
                    .bean("exceptionHandlerBean")
                    .to("{{deadLetterEndpoint}}");

            onException(Exception.class)
                    .maximumRedeliveries(3)
                    .continued(true)
                    .bean("exceptionHandlerBean")
                    .to("{{deadLetterEndpoint}}");


            from("direct:onExceptionRouteNoContinue")
                    .bean("throwBean")
                    .bean("bean2")
                    .bean("bean3")
                    .to("{{successEndpoint}}");


            from("direct:onExceptionRouteContinue")
                    .bean("throwBean")
                    .bean("bean2")
                    .bean("bean3")
                    .to("{{successEndpoint}}");
        }

}
