package com.mkdirjava.route;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


class TryCatchFinallyRouteTest  extends CamelTestSupport {

    @Override
    protected RoutesBuilder[] createRouteBuilders() {
        return new RoutesBuilder[] { new TryCatchFinallyRoute()};
    }

    @Test
    void tryCatchRouteHandler(){
        template.sendBody("direct:tryCatchRoute","home");
    }

    @Test
    void globalRouteHandler(){
        assertThrows(Exception.class,()->{
            template.sendBody("direct:globalErrorRoute","home");
        });
    }
}