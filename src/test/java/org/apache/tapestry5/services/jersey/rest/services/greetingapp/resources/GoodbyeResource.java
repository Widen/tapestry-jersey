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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.tapestry5.services.jersey.rest.services.greetingapp.GreetingService;
import org.apache.tapestry5.services.jersey.rest.services.greetingapp.entities.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/goodbye")
public class GoodbyeResource
{

    private final Logger log = LoggerFactory.getLogger(GoodbyeResource.class);

    private final GreetingService greetingService;

    public GoodbyeResource(GreetingService greetingService)
    {
        this.greetingService = greetingService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting doIt(@Context HttpHeaders headers, @Context Request request, @Context UriInfo uriInfo, @Context SecurityContext securityContext)
    {
        log.info("Request verb: {}", request.getMethod());

        log.info("Request URI: {}", uriInfo.getRequestUri());

        log.info("Security context: {}", securityContext);

        for (String key : headers.getRequestHeaders().keySet())
        {
            log.info("Header {}: {}", key, headers.getRequestHeader(key));
        }

        return greetingService.getHelloGreeting("Doris", "Schutt", "The answer to life the universe and everything...");
    }

}
