package org.apache.tapestry5.services.jersey.providers.gson;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

public class GmtDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>
{

    private final List<String> formats = Lists.newArrayList("yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Override
    public synchronized JsonElement serialize(Date date, Type type,
                                              JsonSerializationContext jsonSerializationContext)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(formats.get(0));
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new JsonPrimitive(sdf.format(date));
    }

    @Override
    public synchronized Date deserialize(JsonElement jsonElement, Type type,
                                         JsonDeserializationContext jsonDeserializationContext)
    {
        for (String f : formats)
        {
            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat(f);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                return sdf.parse(jsonElement.getAsString());
            }
            catch (ParseException ignored)
            {
            }
        }

        throw new JsonSyntaxException("Date is not parsable:" + jsonElement.getAsString());
    }

}
