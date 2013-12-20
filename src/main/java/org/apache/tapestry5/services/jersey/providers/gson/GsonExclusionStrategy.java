package org.apache.tapestry5.services.jersey.providers.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonExclusionStrategy implements ExclusionStrategy
{

    @Override
    public boolean shouldSkipClass(Class<?> clazz)
    {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes field)
    {
        return field.getAnnotation(ExcludeFromJson.class) != null;
    }

}
