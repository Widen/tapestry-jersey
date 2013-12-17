package org.apache.tapestry5.services.jersey;

public class JerseyStatusCodeResponseException extends RuntimeException
{

    private final int statusCode;

    public JerseyStatusCodeResponseException(String message, int statusCode)
    {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

}
