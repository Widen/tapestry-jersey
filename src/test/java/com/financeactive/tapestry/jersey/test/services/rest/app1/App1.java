package com.financeactive.tapestry.jersey.test.services.rest.app1;

import org.apache.tapestry5.ioc.annotations.Inject;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@ApplicationPath("/rest/app1")
public class App1 extends Application {

    @Inject
    private HelloWorld helloWorld;

    @Override
    public Set<Object> getSingletons() {
        return new HashSet<Object>(
                Arrays.asList(helloWorld)
        );
    }
}
