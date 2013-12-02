package org.apache.tapestry5.services.jersey.rest.app2;

public enum Color
{

    RED,
    BLUE,
    GREEN,
    PURPLE;

    @Override
    public String toString()
    {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }

}
