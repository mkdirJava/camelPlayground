package com.mkdirjava.route;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

class OnErrorHandlerHandledTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder[] createRouteBuilders() {
        return new OnErrorHandlerHandled[]{new OnErrorHandlerHandled()};
    }

    @Test
    void testHandler(){
        template.sendBody("direct:globalErrorRoute","home");
    }

}