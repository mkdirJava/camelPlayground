package com.mkdirjava.route.flow.error;

import com.mkdirjava.bean.Bean;
import com.mkdirjava.exception.ExceptionOne;
import com.mkdirjava.route.flow.error.OnExceptionRoute;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class OnExceptionRouteTest extends CamelTestSupport {

    @Mock
    Bean throwBean;
    @Mock
    Bean bean2;
    @Mock
    Bean bean3;
    @Mock
    Bean exceptionHandlerBean;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        super.beforeEach(context);
        MockitoAnnotations.openMocks(this);
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        Registry registry = camelContext.getRegistry();
        registry.bind("throwBean",throwBean);
        registry.bind("bean2",bean2);
        registry.bind("bean3",bean3);
        registry.bind("exceptionHandlerBean",exceptionHandlerBean);

        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("onExceptionTest.properties");
        camelContext.setPropertiesComponent(propertiesComponent);

        return camelContext;
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders()  {
        return new RoutesBuilder[]{new OnExceptionRoute()};
    }

    @Test
    void testOnExceptionNoContinue() throws Exception {
        MockEndpoint deadLetterEndpoint = getMockEndpoint("mock:deadLetterEndpoint");
        doThrow(new ExceptionOne("random Exception")).when(throwBean).doSomething(any(),any());
        template.sendBody("direct:onExceptionRouteNoContinue","home");

        verify(throwBean,times(4)).doSomething(any(),any());
        verify(exceptionHandlerBean).doSomething(any(),any());
        verifyNoMoreInteractions(bean2);
        verifyNoMoreInteractions(bean3);
        verifyNoMoreInteractions(throwBean);
        verifyNoMoreInteractions(exceptionHandlerBean);

        deadLetterEndpoint.expectedMessageCount(1);
        assertMockEndpointsSatisfied(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnExceptionContinue() throws Exception {
        MockEndpoint deadLetterEndpoint = getMockEndpoint("mock:deadLetterEndpoint");
        doThrow(new Exception("random Exception")).when(throwBean).doSomething(any(),any());
        template.sendBody("direct:onExceptionRouteContinue","home");

        verify(throwBean,times(4)).doSomething(any(),any());
        verify(exceptionHandlerBean).doSomething(any(),any());
        verify(bean2).doSomething(any(),any());
        verify(bean3).doSomething(any(),any());

        verifyNoMoreInteractions(bean2);
        verifyNoMoreInteractions(bean3);
        verifyNoMoreInteractions(throwBean);
        verifyNoMoreInteractions(exceptionHandlerBean);

        deadLetterEndpoint.expectedMessageCount(1);
        assertMockEndpointsSatisfied(2, TimeUnit.SECONDS);
    }
}