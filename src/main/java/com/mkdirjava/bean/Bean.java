package com.mkdirjava.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;

public class Bean {
    @Handler
    public void doSomething(String body, Exchange exchange) throws Exception{

    }
}
