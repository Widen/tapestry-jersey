package com.financeactive.tapestry.jersey.test.services.rest.app2;

import org.apache.tapestry5.ioc.annotations.Inject;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@ApplicationPath("/rest/app2")
public class App2 extends Application {

    @Inject
    private Coucou coucou;

    @Override
    public Set<Object> getSingletons() {
        return new HashSet<Object>(
                Arrays.asList(coucou)
        );
    }
}
