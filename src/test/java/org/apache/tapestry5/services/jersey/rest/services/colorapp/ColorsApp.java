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

package org.apache.tapestry5.services.jersey.rest.services.colorapp;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.jersey.ContainerRequestContextProvider;
import org.apache.tapestry5.services.jersey.TapestryBackedJerseyApplication;
import org.apache.tapestry5.services.jersey.internal.JerseyTapestryRequestContext;
import org.apache.tapestry5.services.jersey.providers.JerseyCheckForUpdatesProviderFilter;
import org.apache.tapestry5.services.jersey.providers.ValueEncoderSourceParamConverterProvider;
import org.glassfish.jersey.filter.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationPath("/api2")
public class ColorsApp extends TapestryBackedJerseyApplication
{

    private final Logger log = LoggerFactory.getLogger(ColorsApp.class);

    private final Set<Object> singletonResources = new HashSet<Object>();

    private final boolean productionMode;

    private final JerseyCheckForUpdatesProviderFilter updatesProvider;

    @Inject
    private ColorsResource colorsResource;

    public ColorsApp(@Inject @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode,
                     JerseyTapestryRequestContext requestContext,
                     ContainerRequestContextProvider containerRequestContextProvider,
                     JerseyCheckForUpdatesProviderFilter updatesProvider,
                     ValueEncoderSourceParamConverterProvider converterProvider)
    {
        super(requestContext, containerRequestContextProvider);
        this.productionMode = productionMode;
        this.updatesProvider = updatesProvider;

        this.singletonResources.add(converterProvider);
    }

    @Override
    public Set<Object> getSingletons()
    {
        singletonResources.add(colorsResource);

        if (!productionMode)
        {
            log.info("Adding T5 service re-loader provider to {}", this.getClass().getName());
            singletonResources.add(updatesProvider);
        }

        return singletonResources;
    }

    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(ImageResource.class);
        classes.add(LoggingFilter.class);
        return classes;
    }
}
