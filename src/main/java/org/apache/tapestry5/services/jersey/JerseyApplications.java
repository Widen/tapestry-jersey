package org.apache.tapestry5.services.jersey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyApplications
{

    private Map<String, JerseyEndpoint> applications = new HashMap<String, JerseyEndpoint>();

    private static final Logger log = LoggerFactory.getLogger(JerseyModule.class);

    private final List<String> paths = new ArrayList<String>();

    public JerseyApplications(Collection<Application> configuration, @Inject TapestryRequestContext tapestryRequestContext)
    {
        if (configuration != null)
        {
            for (Application application : configuration)
            {
                ApplicationPath path = application.getClass().getAnnotation(ApplicationPath.class);

                if (path == null)
                {
                    throw new IllegalArgumentException("You must set the ApplicationPath on all registered applications: " + application.getClass().getName());
                }

                log.info("Adding JAX-WS path '{}' for application '{}'", path.value(), application.getClass().getName());

                verify(path.value());
                applications.put(path.value(), new JerseyEndpoint(path.value(), application, tapestryRequestContext));
            }
        }
    }

    private void verify(String path)
    {
        if (paths.contains(path))
        {
            throw new RuntimeException(String.format("Path '%s' has already been assigned to a Jersey application", path));
        }

        paths.add(path);
    }

    public Collection<JerseyEndpoint> getEndPoints()
    {
        return applications.values();
    }

}
