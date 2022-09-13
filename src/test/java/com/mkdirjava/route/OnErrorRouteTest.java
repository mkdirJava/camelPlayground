package com.mkdirjava.route;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;


class OnErrorRouteTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[]{new OnErrorRoute()};
    }

    @Test
    void testHandler(){
        template.sendBody("direct:globalErrorRoute","home");
    }
}