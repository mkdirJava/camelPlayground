package com.mkdirjava.route.flow.error;

import com.mkdirjava.bean.Bean;
import com.mkdirjava.exception.ExceptionOne;
import com.mkdirjava.route.flow.error.TryCatchFinallyRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spi.Registry;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class TryCatchFinallyRouteTest  extends CamelTestSupport {

    @Mock
    Bean throwExceptionOneBean;
    @Mock
    Bean errorCatchingBean;
    @Mock
    Bean finalBean;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        super.beforeEach(context);
        MockitoAnnotations.openMocks(this);
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("./tryCatchFinallyRouteTest.properties");
        camelContext.setPropertiesComponent(propertiesComponent);
        Registry registry = camelContext.getRegistry();
        registry.bind("throwExceptionOneBean", throwExceptionOneBean);
        registry.bind("errorCatchingBean",errorCatchingBean);
        registry.bind("finalBean",finalBean);
        return camelContext;
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() {
        return new RoutesBuilder[] { new TryCatchFinallyRoute()};
    }

    /*
    In a try catch block, should an exception not be handled in the catch,
    an exception will be thrown regardless of the error handler
    the final block is still executed
     */
    @Test
    void tryCatchRouteDoesNotHandleGlobalErrorHandlers() throws InterruptedException {
        MockEndpoint deadLetterEndpoint = getMockEndpoint("mock:deadLetter");
        assertThrows(Exception.class,()-> {
            template.sendBody("direct:tryCatchRoute", TryCatchFinallyRoute.UNHANDLED_EXCEPTION);
        });
        deadLetterEndpoint.expectedMessageCount(0);
        assertMockEndpointsSatisfied(2,TimeUnit.SECONDS);
    }

    @Test
    void globalRouteHandler() throws Exception {
        doThrow(new ExceptionOne("Something went wrong")).when(throwExceptionOneBean).doSomething(any(),any());

        MockEndpoint goodMockEndpoint = getMockEndpoint("mock:goodEndpoint");

        template.sendBody("direct:tryCatchRoute","This will trigger something to throw a catching exception");

        verify(throwExceptionOneBean).doSomething(any(),any());
        verify(errorCatchingBean).doSomething(any(),any());
        verify(finalBean).doSomething(any(),any());
        verifyNoMoreInteractions(throwExceptionOneBean);
        verifyNoMoreInteractions(errorCatchingBean);
        verifyNoMoreInteractions(finalBean);

        goodMockEndpoint.expectedMessageCount(1);
        assertMockEndpointsSatisfied(2, TimeUnit.SECONDS);
    }
}