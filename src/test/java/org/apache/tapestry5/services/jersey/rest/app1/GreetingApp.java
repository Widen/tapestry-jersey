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

package org.apache.tapestry5.services.jersey.rest.app1;

import java.util.Collections;
import java.util.Set;
import javax.ws.rs.ApplicationPath;

import com.google.common.collect.Sets;
import org.apache.tapestry5.services.jersey.TapestryBackedJerseyApplication;
import org.apache.tapestry5.services.jersey.internal.JerseyTapestryRequestContext;
import org.apache.tapestry5.services.jersey.providers.gson.GsonMessageBodyHandler;
import org.apache.tapestry5.services.jersey.rest.app1.resources.GoodbyeResource;
import org.apache.tapestry5.services.jersey.rest.app1.resources.HelloResource;
import org.glassfish.jersey.filter.LoggingFilter;

@ApplicationPath("/api")
public class GreetingApp extends TapestryBackedJerseyApplication
{

    private final GsonMessageBodyHandler gsonMessageBodyHandler;

    private final HelloResource helloResource;

    private final GoodbyeResource goodbyeResource;

    public GreetingApp(JerseyTapestryRequestContext requestContext,
                       GsonMessageBodyHandler gsonMessageBodyHandler,
                       HelloResource helloResource, GoodbyeResource goodbyeResource)
    {
        super(requestContext);
        this.gsonMessageBodyHandler = gsonMessageBodyHandler;
        this.helloResource = helloResource;
        this.goodbyeResource = goodbyeResource;
    }

    @Override
    public Set<Object> getSingletons()
    {
        return Sets.newHashSet(gsonMessageBodyHandler, helloResource, goodbyeResource, new LoggingFilter());
    }

    @Override
    public Set<Class<?>> getClasses()
    {
        return Collections.emptySet();
    }

}
