package com.mkdirjava.route;

import com.mkdirjava.route.OnErrorHandlerContinuedRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

class OnErrorHandlerRouteTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder[] createRouteBuilders() {
        return new OnErrorHandlerContinuedRoute[]{new OnErrorHandlerContinuedRoute()};
    }

    @Test
    void testHandler(){
        template.sendBody("direct:globalErrorRoute","home");
    }

}