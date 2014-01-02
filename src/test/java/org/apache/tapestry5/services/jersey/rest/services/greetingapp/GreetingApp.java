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

package org.apache.tapestry5.services.jersey.rest.services.greetingapp;

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
import org.apache.tapestry5.services.jersey.providers.gson.GsonMessageBodyHandler;
import org.apache.tapestry5.services.jersey.rest.services.greetingapp.resources.GoodbyeResource;
import org.apache.tapestry5.services.jersey.rest.services.greetingapp.resources.HelloResource;
import org.glassfish.jersey.filter.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationPath("/api")
public class GreetingApp extends TapestryBackedJerseyApplication
{

    private final Logger log = LoggerFactory.getLogger(GreetingApp.class);

    private final boolean productionMode;

    private final JerseyCheckForUpdatesProviderFilter updatesProvider;

    private final GsonMessageBodyHandler gsonMessageBodyHandler;

    private final HelloResource helloResource;

    private final GoodbyeResource goodbyeResource;

    public GreetingApp(@Inject @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode,
                       JerseyTapestryRequestContext requestContext,
                       ContainerRequestContextProvider containerRequestContextProvider,
                       JerseyCheckForUpdatesProviderFilter updatesProvider,
                       GsonMessageBodyHandler gsonMessageBodyHandler,
                       HelloResource helloResource,
                       GoodbyeResource goodbyeResource)
    {
        super(requestContext, containerRequestContextProvider);
        this.productionMode = productionMode;
        this.updatesProvider = updatesProvider;
        this.gsonMessageBodyHandler = gsonMessageBodyHandler;
        this.helloResource = helloResource;
        this.goodbyeResource = goodbyeResource;
    }

    @Override
    public Set<Object> getSingletons()
    {
        Set<Object> singletons = new HashSet<Object>();

        singletons.add(gsonMessageBodyHandler);
        singletons.add(helloResource);
        singletons.add(goodbyeResource);

        if (!productionMode)
        {
            log.info("Adding T5 service re-loader provider");
            singletons.add(updatesProvider);
        }

        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> classes = new HashSet<Class<?>>();

        classes.add(LoggingFilter.class);

        return classes;
    }

}
