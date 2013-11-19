package org.apache.tapestry5.services.jersey.rest.app1;

public class HelloResponseResourceImpl implements HelloResponseResource
{

    @Override
    public HelloResponse getHelloResponse()
    {
        return new HelloResponse("Johnny", "Appleseed", "An apple a day...");
    }

}
