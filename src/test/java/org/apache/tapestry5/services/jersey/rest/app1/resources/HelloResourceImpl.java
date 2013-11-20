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

package org.apache.tapestry5.services.jersey.rest.app1.resources;

import org.apache.tapestry5.services.jersey.rest.app1.entities.Greeting;

public class HelloResourceImpl implements HelloResource
{

    @Override
    public Greeting getHelloResponse()
    {
        return new Greeting("Johnny", "Appleseed", "An apple a day keeps...");
    }

}
