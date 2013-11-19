package org.apache.tapestry5.services.jersey.rest.app1;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.google.common.collect.Sets;

@ApplicationPath("/api")
public class HelloApp extends Application
{

    private final HelloService helloService;

    private final HelloResponseResource helloResponseResource;

    public HelloApp(HelloService helloService, HelloResponseResource helloResponseResource)
    {
        this.helloService = helloService;
        this.helloResponseResource = helloResponseResource;
    }

    @Override
    public Set<Object> getSingletons()
    {
        return Sets.newHashSet(helloService, helloResponseResource);
    }

}
