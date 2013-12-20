package org.apache.tapestry5.services.jersey.providers.gson;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

public class DateTimeTypeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime>
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
