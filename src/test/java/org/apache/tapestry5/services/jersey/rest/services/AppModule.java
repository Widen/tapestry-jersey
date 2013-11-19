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

package org.apache.tapestry5.services.jersey.rest.services;

import javax.ws.rs.core.Application;

import org.apache.tapestry5.services.jersey.rest.app1.HelloApp;
import org.apache.tapestry5.services.jersey.rest.app1.HelloResponseResource;
import org.apache.tapestry5.services.jersey.rest.app1.HelloResponseResourceImpl;
import org.apache.tapestry5.services.jersey.rest.app1.HelloService;
import org.apache.tapestry5.services.jersey.rest.app1.HelloServiceImpl;
import org.apache.tapestry5.services.jersey.rest.app2.GoodbyeApp;
import org.apache.tapestry5.services.jersey.rest.app2.GoodbyeService;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.jersey.JerseyModule;

@SubModule(JerseyModule.class)
public class AppModule
{

    public static void bind(ServiceBinder binder)
    {
        binder.bind(HelloApp.class);
        binder.bind(HelloService.class, HelloServiceImpl.class);
        binder.bind(HelloResponseResource.class, HelloResponseResourceImpl.class);

        binder.bind(GoodbyeApp.class);
        binder.bind(GoodbyeService.class);
    }

    public static void contributeJerseyApplications(Configuration<Application> configuration, HelloApp helloApp, GoodbyeApp goodbyeApp)
    {
        configuration.add(helloApp);
        //configuration.add(goodbyeApp);
    }
}
