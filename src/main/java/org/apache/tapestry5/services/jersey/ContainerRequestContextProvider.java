package org.apache.tapestry5.services.jersey;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Expose JAX-RS ContainerRequestContext object to Tapestry IOC.
 * <p/>
 * Note: Do <b>not</b> inject this object, but <code>javax.ws.rs.container.ContainerRequestContext</code> directly.
 * A shadow builder is registered to expose the interface directly.
 */
public interface ContainerRequestContextProvider
{

    public ContainerRequestContext getContainerRequestContext();

    public void setContainerRequestContext(ContainerRequestContext containerRequestContext);

}
