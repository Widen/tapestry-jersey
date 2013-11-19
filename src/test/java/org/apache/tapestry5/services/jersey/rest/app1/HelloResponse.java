package org.apache.tapestry5.services.jersey.rest.app1;

public class HelloResponse
{

    private String first_name;

    private String last_name;

    private String quote;

    public HelloResponse(String first_name, String last_name, String quote)
    {
        this.first_name = first_name;
        this.last_name = last_name;
        this.quote = quote;
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }

    public String getQuote()
    {
        return quote;
    }

    public void setQuote(String quote)
    {
        this.quote = quote;
    }

}
