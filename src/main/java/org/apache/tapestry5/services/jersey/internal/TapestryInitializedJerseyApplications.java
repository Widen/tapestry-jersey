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

package org.apache.tapestry5.services.jersey.internal;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.tapestry5.services.jersey.TapestryBackedJerseyApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TapestryInitializedJerseyApplications
{

    private Map<String, TapestryBackedJerseyApplication> apps = new ConcurrentHashMap<String, TapestryBackedJerseyApplication>(4);

    private static final Logger log = LoggerFactory.getLogger(TapestryInitializedJerseyApplications.class);

    public TapestryInitializedJerseyApplications(Collection<TapestryBackedJerseyApplication> configuration, JerseyTapestryRequestContext requestContext)
    {
        for (TapestryBackedJerseyApplication application : configuration)
        {
            log.info("Assigning path prefix '{}' to JAX-RS application {}", application.getAppPath(), application.getClass().getName());

            checkForDuplicateAppPath(application.getClass().getName(), application.getAppPath());
            application.configureJaxRsApplication();

            apps.put(application.getAppPath(), application);
        }
    }

    private void checkForDuplicateAppPath(String appName, String path)
    {
        for (TapestryBackedJerseyApplication endpoint : apps.values())
        {
            if (endpoint.getAppPath().equals(path))
            {
                throw new RuntimeException(String.format("Path '%s' has already been assigned to the JAX-RS application %s", path, appName));
            }
        }
    }

    public Collection<TapestryBackedJerseyApplication> getApplications()
    {
        return apps.values();
    }

}
