package org.apache.tapestry5.services.jersey.internal;

import javax.ws.rs.container.ContainerRequestContext;

import org.apache.tapestry5.services.jersey.ContainerRequestContextProvider;

public class ContainerRequestContextProviderImpl implements ContainerRequestContextProvider
{

    private ContainerRequestContext containerRequestContext;

    public ContainerRequestContextProviderImpl()
    {
    }

    @Override
    public ContainerRequestContext getContainerRequestContext()
    {
        return containerRequestContext;
    }

    @Override
    public void setContainerRequestContext(ContainerRequestContext containerRequestContext)
    {
        this.containerRequestContext = containerRequestContext;
    }

}
