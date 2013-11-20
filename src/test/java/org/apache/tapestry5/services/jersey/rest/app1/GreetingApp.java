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

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.google.common.collect.Sets;
import org.apache.tapestry5.services.jersey.rest.app1.resources.GoodbyeResource;
import org.apache.tapestry5.services.jersey.rest.app1.resources.HelloResource;

@ApplicationPath("/api")
public class GreetingApp extends Application
{

    private final HelloResource helloResource;

    private final GoodbyeResource goodbyeResource;

    public GreetingApp(HelloResource helloResource, GoodbyeResource goodbyeResource)
    {
        this.helloResource = helloResource;
        this.goodbyeResource = goodbyeResource;
    }

    @Override
    public Set<Object> getSingletons()
    {
        return Sets.newHashSet(helloResource, goodbyeResource);
    }

}