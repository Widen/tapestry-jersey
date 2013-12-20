// Copyright 2013 SAP AG
//
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use
// this work except in compliance with the License. You may obtain a copy of the
// License in the LICENSE file, or at:
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

package org.apache.tapestry5.services.jersey.providers.gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;

/**
 * https://github.com/SAP/cloud-paulpredicts/blob/master/src/main/java/com/sap/pto/services/util/GsonMessageBodyHandler.java
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GsonMessageBodyHandler<T> implements MessageBodyWriter<T>, MessageBodyReader<T>
{

    private final Gson gson;

    public GsonMessageBodyHandler(JerseyGsonBuilder builder)
    {
        gson = builder.getBuilder().create();
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return true;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return true;
    }

    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                      MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException
    {

        Reader entityReader = new InputStreamReader(entityStream, "UTF-8");
        Type targetType;
        if (Collection.class.isAssignableFrom(type))
        {
            targetType = genericType;
        }
        else
        {
            targetType = type;
        }

        return gson.fromJson(entityReader, targetType);
    }

    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {

        // convert all except Strings
        if (!String.class.isAssignableFrom(type))
        {
            entityStream.write(gson.toJson(t).getBytes("UTF-8"));
        }
        else
        {
            entityStream.write(((String) t).getBytes("UTF-8"));
        }
    }

}
