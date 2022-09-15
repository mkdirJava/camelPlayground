package com.mkdirjava.route.flow.error;

import com.mkdirjava.bean.Bean;
import com.mkdirjava.route.flow.error.ErrorHandler;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ErrorHandlerTest  extends CamelTestSupport {

    @Mock
    Bean throwBean;
    @Mock
    Bean exceptionHandlerBean;
    @Mock
    Bean bean2;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        super.beforeEach(context);
        MockitoAnnotations.openMocks(this);
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("errorHandlerTest.properties");
        camelContext.setPropertiesComponent(propertiesComponent);

        Registry registry = camelContext.getRegistry();
        registry.bind("globalErrorHandlerBean",exceptionHandlerBean);
        registry.bind("routeErrorHandlerBean",exceptionHandlerBean);
        registry.bind("throwBean",throwBean);
        return camelContext;
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() {
        return new RoutesBuilder[] {new ErrorHandler()};
    }

    @Test
    void shouldUseGlobalHandler() throws Exception {

        MockEndpoint successMockEndpoint = getMockEndpoint("mock:successEndpoint");

        doThrow(new Exception("home")).when(throwBean).doSomething(any(),any());

        template.sendBody("direct:globalError","hi");

        verify(throwBean,times(4)).doSomething(any(),any());
        verify(exceptionHandlerBean).doSomething(any(),any());
        verifyNoMoreInteractions(throwBean);
        verifyNoMoreInteractions(exceptionHandlerBean);

        verifyNoInteractions(bean2);

        successMockEndpoint.expectedMessageCount(0);

    }

    @Test
    void shouldUseRouteSpecificHandler() throws Exception {


        MockEndpoint successMockEndpoint = getMockEndpoint("mock:successEndpoint");

        doThrow(new Exception("home")).when(throwBean).doSomething(any(),any());

        template.sendBody("direct:routeError","hi");

        verify(throwBean).doSomething(any(),any());
        verify(exceptionHandlerBean).doSomething(any(),any());
        verifyNoMoreInteractions(throwBean);
        verifyNoMoreInteractions(exceptionHandlerBean);

        verifyNoInteractions(bean2);

        successMockEndpoint.expectedMessageCount(0);


    }
}