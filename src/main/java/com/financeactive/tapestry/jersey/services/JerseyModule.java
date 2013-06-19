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

package com.financeactive.tapestry.jersey.services;

import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.services.PipelineBuilder;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogManager;

public class JerseyModule {

    static {
        // Jersey uses java.util.logging - bridge to slf4
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        SLF4JBridgeHandler.install();
    }

    private static final Logger LOG = LoggerFactory.getLogger(JerseyModule.class);

    public static void bind(ServiceBinder binder) {
        binder.bind(TapestryRequestContext.class);
        binder.bind(JerseyApplications.class);
        binder.bind(HttpServletRequestFilter.class, JerseyHttpRequestFilter.class)
                .withId("JerseyHttpServletRequestFilter");
    }

    /**
     * Added {@link JerseyHttpRequestFilter} to the very beginning of servlet filter chain.
     *
     * @param configuration
     * @param jerseyFilter
     */
    @Contribute(HttpServletRequestHandler.class)
    public void contributeHttpServletRequestHandler(
            OrderedConfiguration<HttpServletRequestFilter> configuration,
            @Service("JerseyHttpServletRequestFilter") HttpServletRequestFilter jerseyFilter) {
        LOG.info("Integrating Jersey as HTTP request filter.");
        configuration.add("JerseyFilter", jerseyFilter,
                "after:StoreIntoGlobals",
                "after:SecurityConfiguration",
                "after:SecurityRequestFilter",
                "before:EndOfRequest", "before:GZIP");
    }

    public JerseyRequestHandler buildJerseyRequestHandler(@InjectService("PipelineBuilder") PipelineBuilder builder,
                                          @Inject ObjectLocator locator,
                                          List<JerseyRequestFilter> configuration, Logger logger) {

        JerseyRequestHandler terminator = locator.autobuild(JerseyHttpRequestFilter.Terminator.class);

        return builder.build(logger, JerseyRequestHandler.class, JerseyRequestFilter.class, configuration, terminator);
    }

}
