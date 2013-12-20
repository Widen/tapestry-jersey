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

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.jersey.JerseyModule;
import org.apache.tapestry5.services.jersey.TapestryBackedJerseyApplication;
import org.apache.tapestry5.services.jersey.rest.app1.GreetingApp;
import org.apache.tapestry5.services.jersey.rest.app1.resources.GoodbyeResource;
import org.apache.tapestry5.services.jersey.rest.app1.resources.HelloResource;
import org.apache.tapestry5.services.jersey.rest.app1.resources.HelloResourceImpl;
import org.apache.tapestry5.services.jersey.rest.app1.services.SimpleService;
import org.apache.tapestry5.services.jersey.rest.app1.services.SimpleServiceImpl;
import org.apache.tapestry5.services.jersey.rest.app2.ColorsApp;
import org.apache.tapestry5.services.jersey.rest.app2.ColorsResource;

@SubModule(JerseyModule.class)
public class TestAppModule
{

    public static void bind(ServiceBinder binder)
    {
        binder.bind(SimpleService.class, SimpleServiceImpl.class);

        binder.bind(GreetingApp.class);
        binder.bind(HelloResource.class, HelloResourceImpl.class);
        binder.bind(GoodbyeResource.class);

        binder.bind(ColorsApp.class);
        binder.bind(ColorsResource.class);
    }

    public static void contributeTapestryInitializedJerseyApplications(Configuration<TapestryBackedJerseyApplication> configuration, GreetingApp greetingApp, ColorsApp colorsApp)
    {
        configuration.add(greetingApp);
        configuration.add(colorsApp);
    }

}
