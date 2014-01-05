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

package org.apache.tapestry5.services.jersey.rest.services.greetingapp.resources;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.jersey.rest.services.greetingapp.GreetingService;
import org.apache.tapestry5.services.jersey.rest.services.greetingapp.entities.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloResourceImpl implements HelloResource
{

    private final Logger log = LoggerFactory.getLogger(HelloResourceImpl.class);

    @Inject // T5 Injection
    private GreetingService greetingService;

    @Override
    public Greeting getHelloResponse(String first, String last, String phrase, Request request, UriInfo uriInfo)
    {
        log.info("Request TTT: {} {}", request.getMethod(), uriInfo.getRequestUri());
        return greetingService.getHelloGreeting(first, last, phrase);
    }

}
