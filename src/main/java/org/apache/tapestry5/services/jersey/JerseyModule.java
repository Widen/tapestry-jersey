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

import java.util.Date;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.ext.ParamConverterProvider;

import com.google.gson.GsonBuilder;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.services.PipelineBuilder;
import org.apache.tapestry5.ioc.services.PropertyShadowBuilder;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.apache.tapestry5.services.jersey.internal.ContainerRequestContextProviderImpl;
import org.apache.tapestry5.services.jersey.internal.JerseyHttpServletRequestFilter;
import org.apache.tapestry5.services.jersey.internal.JerseyRequestFilter;
import org.apache.tapestry5.services.jersey.internal.JerseyRequestHandler;
import org.apache.tapestry5.services.jersey.internal.JerseyTapestryRequestContext;
import org.apache.tapestry5.services.jersey.internal.TapestryInitializedJerseyApplications;
import org.apache.tapestry5.services.jersey.providers.JerseyCheckForUpdatesProviderFilter;
import org.apache.tapestry5.services.jersey.providers.ValueEncoderSourceParamConverterProvider;
import org.apache.tapestry5.services.jersey.providers.gson.DateTimeTypeConverter;
import org.apache.tapestry5.services.jersey.providers.gson.GmtDateTypeAdapter;
import org.apache.tapestry5.services.jersey.providers.gson.GsonExclusionStrategy;
import org.apache.tapestry5.services.jersey.providers.gson.GsonMessageBodyHandler;
import org.apache.tapestry5.services.jersey.providers.gson.JerseyGsonBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class JerseyModule
{

    static
    {
        // Jersey uses java.util.logging - bridge to slf4j
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static final Logger log = LoggerFactory.getLogger(JerseyModule.class);

    public static void bind(ServiceBinder binder)
    {
        binder.bind(JerseyTapestryRequestContext.class);
        binder.bind(TapestryInitializedJerseyApplications.class);
        binder.bind(ParamConverterProvider.class, ValueEncoderSourceParamConverterProvider.class);
        binder.bind(GsonMessageBodyHandler.class);
        binder.bind(JerseyCheckForUpdatesProviderFilter.class);
        binder.bind(HttpServletRequestFilter.class, JerseyHttpServletRequestFilter.class).withSimpleId();
        binder.bind(ContainerRequestContextProvider.class, ContainerRequestContextProviderImpl.class).scope(ScopeConstants.PERTHREAD);
    }

    public JerseyRequestHandler buildJerseyRequestHandler(PipelineBuilder pipelineBuilder, ServiceResources serviceResources, List<JerseyRequestFilter> configuration, Logger logger)
    {
        JerseyRequestHandler terminator = serviceResources.autobuild(JerseyHttpServletRequestFilter.Terminator.class);
        return pipelineBuilder.build(logger, JerseyRequestHandler.class, JerseyRequestFilter.class, configuration, terminator);
    }

    /**
     * Add {@link JerseyHttpServletRequestFilter} to the request handler chain
     */
    @Contribute(HttpServletRequestHandler.class)
    public void contributeHttpServletRequestHandler(OrderedConfiguration<HttpServletRequestFilter> configuration, @Local HttpServletRequestFilter jerseyFilter)
    {
        log.info("Contributing Jersey JAX-RS as a HTTP request filter...");

        configuration.add("JerseyFilter", jerseyFilter,
                "after:StoreIntoGlobals",
                "before:EndOfRequest",
                "before:GZIP");
    }

    public static JerseyGsonBuilder buildJerseyGsonBuilder()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter());
        builder.registerTypeAdapter(Date.class, new GmtDateTypeAdapter());
        builder.addSerializationExclusionStrategy(new GsonExclusionStrategy());
        builder.setPrettyPrinting();
        builder.disableHtmlEscaping();
        return new JerseyGsonBuilder(builder);
    }

    public ContainerRequestContext buildContainerRequestContext(PropertyShadowBuilder shadowBuilder, ContainerRequestContextProvider provider)
    {
        return shadowBuilder.build(provider, "containerRequestContext", ContainerRequestContext.class);
    }

}
