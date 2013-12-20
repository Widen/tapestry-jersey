package org.apache.tapestry5.services.jersey.providers.gson;

import com.google.gson.GsonBuilder;

public class JerseyGsonBuilder
{

    private final GsonBuilder builder;

    /**
     * Class to isolate Gson instance for Jersey use.
     */
    public JerseyGsonBuilder(GsonBuilder builder)
    {
        this.builder = builder;
    }

    public GsonBuilder getBuilder()
    {
        return builder;
    }

}
