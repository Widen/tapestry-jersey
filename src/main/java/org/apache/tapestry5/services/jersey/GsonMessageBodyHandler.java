package org.apache.tapestry5.services.jersey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
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
import org.joda.time.DateTime;

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
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        builder.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter());
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

    private static class DateTimeTypeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime>
    {
        @Override
        public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context)
        {
            return new JsonPrimitive(src.toString());
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
            return field.getAnnotation(JsonIgnore.class) != null;
        }
    }

}
