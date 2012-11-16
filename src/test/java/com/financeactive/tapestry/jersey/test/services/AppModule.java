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

package com.financeactive.tapestry.jersey.test.services;

import com.financeactive.tapestry.jersey.services.JerseyModule;
import com.financeactive.tapestry.jersey.test.services.rest.app1.App1;
import com.financeactive.tapestry.jersey.test.services.rest.app1.HelloWorld;
import com.financeactive.tapestry.jersey.test.services.rest.app1.HelloWorldImpl;
import com.financeactive.tapestry.jersey.test.services.rest.app2.App2;
import com.financeactive.tapestry.jersey.test.services.rest.app2.Coucou;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;

import javax.ws.rs.core.Application;

@SubModule(JerseyModule.class)
public class AppModule {
    
    public static void bind(ServiceBinder binder) {
        binder.bind(HelloWorld.class, HelloWorldImpl.class);
        binder.bind(App1.class);
        binder.bind(Coucou.class);
        binder.bind(App2.class);
    }

    public static void contributeJerseyApplications(Configuration<Application> configuration,
                                                    App1 app1, App2 app2) {
        configuration.add(app1);
        configuration.add(app2);
    }
}
