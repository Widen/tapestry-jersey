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

package org.apache.tapestry5.services.jersey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

/**
 * https://github.com/SAP/cloud-paulpredicts/blob/master/src/main/java/com/sap/pto/services/util/GsonMessageBodyHandler.java
 */

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GsonMessageBodyHandler<T> implements MessageBodyWriter<T>, MessageBodyReader<T>
{

    private final Gson gson;

    public GsonMessageBodyHandler()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter());
        builder.registerTypeAdapter(Date.class, new GmtDateTypeAdapter());
        builder.setPrettyPrinting();
        builder.addSerializationExclusionStrategy(new GsonExclusionStrategy());

        gson = builder.create();
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

    private static class GmtDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>
    {
        private final DateFormat dateFormat;

        private GmtDateTypeAdapter()
        {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        @Override
        public synchronized JsonElement serialize(Date date, Type type,
                                                  JsonSerializationContext jsonSerializationContext)
        {
            synchronized (dateFormat)
            {
                String dateFormatAsString = dateFormat.format(date);
                return new JsonPrimitive(dateFormatAsString);
            }
        }

        @Override
        public synchronized Date deserialize(JsonElement jsonElement, Type type,
                                             JsonDeserializationContext jsonDeserializationContext)
        {
            try
            {
                synchronized (dateFormat)
                {
                    return dateFormat.parse(jsonElement.getAsString());
                }
            }
            catch (ParseException e)
            {
                throw new JsonSyntaxException(jsonElement.getAsString(), e);
            }
        }
    }

    private static class DateTimeTypeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime>
    {
        @Override
        public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context)
        {
            DateTime utc = new DateTime(src, DateTimeZone.UTC);
            return new JsonPrimitive(ISODateTimeFormat.dateTimeNoMillis().print(utc));
        }

        @Override
        public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
        {
            try
            {
                return new DateTime(json.getAsString());
            }
            catch (IllegalArgumentException e)
            {
                // could also be a java.util.Date
                Date date = context.deserialize(json, Date.class);
                return new DateTime(date);
            }
        }
    }

    private static class GsonExclusionStrategy implements ExclusionStrategy
    {
        @Override
        public boolean shouldSkipClass(Class<?> clazz)
        {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field)
        {
            return field.getAnnotation(JerseyJsonIgnore.class) != null;
        }
    }

}
