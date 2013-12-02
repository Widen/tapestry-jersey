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

import java.util.List;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.services.PipelineBuilder;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class JerseyModule
{

    static
    {
        // Jersey uses java.util.logging - bridge to slf4
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static final Logger log = LoggerFactory.getLogger(JerseyModule.class);

    public static void bind(ServiceBinder binder)
    {
        binder.bind(JerseyTapestryRequestContext.class);
        binder.bind(JerseyApplications.class);
        binder.bind(JerseyTapestryParamConverterProvider.class);
        binder.bind(GsonMessageBodyHandler.class);
        binder.bind(HttpServletRequestFilter.class, JerseyHttpServletRequestFilter.class).withSimpleId();
    }

    /**
     * Added {@link JerseyHttpServletRequestFilter} to the very beginning of servlet filter chain.
     */
    @Contribute(HttpServletRequestHandler.class)
    public void contributeHttpServletRequestHandler(OrderedConfiguration<HttpServletRequestFilter> configuration, @Local HttpServletRequestFilter jerseyFilter)
    {
        log.info("Contributing Jersey JAX-RS as a HTTP request filter...");

        configuration.add("JerseyFilter", jerseyFilter,
                "after:StoreIntoGlobals",
                "after:SecurityConfiguration",
                "after:SecurityRequestFilter",
                "before:EndOfRequest",
                "before:GZIP");
    }

    public JerseyRequestHandler buildJerseyRequestHandler(PipelineBuilder builder, ServiceResources serviceResources, List<JerseyRequestFilter> configuration, Logger logger)
    {
        JerseyRequestHandler terminator = serviceResources.autobuild(JerseyHttpServletRequestFilter.Terminator.class);
        return builder.build(logger, JerseyRequestHandler.class, JerseyRequestFilter.class, configuration, terminator);
    }

}
