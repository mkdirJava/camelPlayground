package com.mkdirjava;

import org.apache.camel.*;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.Main;
import org.apache.camel.spi.Registry;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("application.properties");
        CamelContext camelContext = main.getCamelContext();
        Registry registry = camelContext.getRegistry();
        registry.bind("ACTION",new Object());
        camelContext.setPropertiesComponent(propertiesComponent);
        main.configure().addRoutesBuilder(new MyRouteBuilder());
        main.run(args);
    }

}

