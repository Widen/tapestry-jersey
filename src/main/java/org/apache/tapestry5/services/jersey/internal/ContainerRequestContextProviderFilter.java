package org.apache.tapestry5.services.jersey.internal;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.apache.tapestry5.services.jersey.ContainerRequestContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@PreMatching
@Priority(Priorities.USER)
public class ContainerRequestContextProviderFilter implements ContainerRequestFilter
{

    private final Logger log = LoggerFactory.getLogger(ContainerRequestContextProviderFilter.class);

    private final ContainerRequestContextProvider contextProvider;

    public ContainerRequestContextProviderFilter(ContainerRequestContextProvider contextProvider)
    {
        this.contextProvider = contextProvider;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException
    {
        contextProvider.setContainerRequestContext(requestContext);
    }

}
