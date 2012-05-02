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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * Application that offers tapestry managed services to Jersey Application.
 * 
 */
public class TapestryEnabledJAXRSApplication extends Application {

    private Set<Object> singletons;

    private Set<Class<?>> classes;

    /**
     * 
     * @param singletons collection of Tapestry managed services and/or any thread-safe services.
     * Since Tapestry managed per-thread services are thread-safe services, you can add them to this
     * collection too.
     *
     */
    public TapestryEnabledJAXRSApplication(Collection<?> singletons,
                                           Collection<Class<?>> classes) {
        this.singletons = new HashSet<Object>(singletons);
        this.classes = new HashSet<Class<?>>(classes);
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Set<java.lang.Class<?>> getClasses() {
        return classes;
    }
        
}
