// Copyright 2007, 2008, 2009 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.services.jersey;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyApplications
{

    private Map<String, JerseyEndpoint> endpoints = new ConcurrentHashMap<String, JerseyEndpoint>();

    private static final Logger log = LoggerFactory.getLogger(JerseyApplications.class);

    public JerseyApplications(Collection<Application> configuration, @Inject JerseyTapestryRequestContext jerseyTapestryRequestContext)
    {
        if (configuration != null)
        {
            for (Application application : configuration)
            {
                ApplicationPath path = application.getClass().getAnnotation(ApplicationPath.class);

                if (path == null)
                {
                    throw new IllegalArgumentException("No @ApplicationPath configured for application " + application.getClass().getName());
                }

                log.info("Assigning path prefix '{}' to JAX-RS application {}", path.value(), application.getClass().getName());

                verify(application, path.value());
                endpoints.put(path.value(), new JerseyEndpoint(path.value(), application, jerseyTapestryRequestContext));
            }
        }
    }

    private void verify(Application app, String path)
    {
        for (JerseyEndpoint endpoint : endpoints.values())
        {
            if (endpoint.getPath().equals(path))
            {
                throw new RuntimeException(String.format("Path '%s' has already been assigned to the JAX-RS application %s", path, app.getClass().getName()));
            }
        }
    }

    public Collection<JerseyEndpoint> getEndpoints()
    {
        return endpoints.values();
    }

}
