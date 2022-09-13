package com.mkdirjava;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spi.Registry;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;

class PracticeRouteTest  extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        Registry registry = camelContext.getRegistry();
        registry.bind("ACTION", new Object(){
            void action(Exchange exchange){
                System.out.println(exchange.getMessage().getBody());
            }
        });
        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("./test.properties");
        camelContext.setPropertiesComponent(propertiesComponent);
        return camelContext;
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() {
        return new PracticeRoute[]{new PracticeRoute()};
    }

    @Test
    void sendingAHiMessageShouldGoToHi() throws InterruptedException {

        MockEndpoint hiEndEndpoint = getMockEndpoint("mock:hiEnd");
        MockEndpoint homeEndEndpoint = getMockEndpoint("mock:homeEnd");
        MockEndpoint otherwiseEndpoint = getMockEndpoint("mock:otherwise");

        template.sendBody("direct:PracticeRoute","HI");
        hiEndEndpoint.expectedMessagesMatches((exchange)-> exchange.getMessage().getBody(String.class) == "HI");

        hiEndEndpoint.setFailFast(false);
        hiEndEndpoint.assertIsSatisfied();
        hiEndEndpoint.expectedMessageCount(1);

        homeEndEndpoint.expectedMessageCount(0);
        otherwiseEndpoint.expectedMessageCount(0);

        assertMockEndpointsSatisfied(2, TimeUnit.SECONDS);
        assertMockEndpointsSatisfied();
    }

    @Test
    void sendingAHomeMessageShouldGoToHi() throws InterruptedException {

        MockEndpoint hiEndEndpoint = getMockEndpoint("mock:hiEnd");
        MockEndpoint homeEndEndpoint = getMockEndpoint("mock:homeEnd");
        MockEndpoint otherwiseEndpoint = getMockEndpoint("mock:otherwise");

        template.sendBody("direct:PracticeRoute","HOME");

        homeEndEndpoint.expectedMessagesMatches((exchange)-> exchange.getMessage().getBody(String.class) == "HOME");

        hiEndEndpoint.expectedMessageCount(0);

        homeEndEndpoint.setFailFast(false);
        homeEndEndpoint.assertIsSatisfied();
        homeEndEndpoint.expectedMessageCount(1);

        otherwiseEndpoint.expectedMessageCount(0);

        assertMockEndpointsSatisfied(2, TimeUnit.SECONDS);
        assertMockEndpointsSatisfied();
    }

    @Test
    void sendingAOtherwiseMessageShouldGoToHi() throws InterruptedException {

        MockEndpoint hiEndEndpoint = getMockEndpoint("mock:hiEnd");
        MockEndpoint homeEndEndpoint = getMockEndpoint("mock:homeEnd");
        MockEndpoint otherwiseEndpoint = getMockEndpoint("mock:otherwise");

        template.sendBody("direct:PracticeRoute","OTHERWISE");

        otherwiseEndpoint.expectedMessagesMatches((exchange)-> exchange.getMessage().getBody(String.class) == "OTHERWISE");

        hiEndEndpoint.expectedMessageCount(0);
        homeEndEndpoint.expectedMessageCount(0);

        otherwiseEndpoint.setFailFast(false);
        otherwiseEndpoint.assertIsSatisfied();
        otherwiseEndpoint.expectedMessageCount(1);

        assertMockEndpointsSatisfied(2, TimeUnit.SECONDS);
        assertMockEndpointsSatisfied();
    }
}