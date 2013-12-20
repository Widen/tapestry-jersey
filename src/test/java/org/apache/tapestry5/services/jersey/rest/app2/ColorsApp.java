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

package org.apache.tapestry5.services.jersey.rest.app2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.jersey.TapestryBackedJerseyApplication;
import org.apache.tapestry5.services.jersey.internal.JerseyTapestryRequestContext;
import org.apache.tapestry5.services.jersey.providers.ValueEncoderSourceParamConverterProvider;
import org.glassfish.jersey.filter.LoggingFilter;

@ApplicationPath("/api2")
public class ColorsApp extends TapestryBackedJerseyApplication
{

    @Inject
    private ColorsResource colorsResource;

    private ImageResource imageResource = new ImageResource();

    private final ValueEncoderSourceParamConverterProvider converterProvider;

    public ColorsApp(JerseyTapestryRequestContext requestContext, ValueEncoderSourceParamConverterProvider converterProvider)
    {
        super(requestContext);
        this.converterProvider = converterProvider;
    }

    @Override
    public Set<Object> getSingletons()
    {
        return new HashSet<Object>(Arrays.asList(converterProvider, colorsResource, imageResource, new LoggingFilter()));
    }

}
