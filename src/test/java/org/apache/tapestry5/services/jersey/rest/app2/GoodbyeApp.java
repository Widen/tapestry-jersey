package org.apache.tapestry5.services.jersey.rest.app2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.tapestry5.ioc.annotations.Inject;

/**
 *
 */
@ApplicationPath("/goodbye")
public class GoodbyeApp extends Application
{

    @Inject
    private GoodbyeService goodbyeService;

    @Override
    public Set<Object> getSingletons()
    {
        return new HashSet<Object>(Arrays.asList(goodbyeService));
    }

}
