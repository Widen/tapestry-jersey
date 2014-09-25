package org.apache.tapestry5.services.jersey.providers.gson;

import com.google.gson.*;
import jersey.repackaged.com.google.common.collect.Lists;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
