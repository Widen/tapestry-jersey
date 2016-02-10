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

package org.apache.tapestry5.services.jersey.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyRunner
{

    private static final Logger log = LoggerFactory.getLogger(JettyRunner.class);


    public static void main(String[] args)
    {
        try
        {
            Server server = new Server();
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(8080);
            server.addConnector(connector);

            WebAppContext context = new WebAppContext();
            context.setContextPath("/");
            context.setWar("src/test/webapp");
            context.setInitParameter(SessionManager.__CheckRemoteSessionEncoding, "true"); // Stops Jetty from adding 'jsessionid' URL rewriting into non-local URLs (e.g. Google OpenId redirects)

            server.setHandler(context);

            server.addLifeCycleListener(new AbstractLifeCycle.AbstractLifeCycleListener()
            {
                @Override
                public void lifeCycleStarted(LifeCycle event)
                {
                    log.warn("Jetty ready to accept requests...");
                }
            });

            server.start();
            server.join();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error launching Jetty", e);
        }
    }

}
