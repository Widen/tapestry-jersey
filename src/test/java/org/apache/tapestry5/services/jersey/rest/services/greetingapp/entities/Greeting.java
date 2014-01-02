// Copyright 2007, 2008, 2009 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.services.jersey.rest.services.greetingapp.entities;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;

public class Greeting
{

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String quote;

    private Date javaDate;

    private DateTime jodaTime;

    public Greeting(String firstName, String lastName, String quote)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.quote = quote;
        this.javaDate = new Date();
        this.jodaTime = new DateTime(this.javaDate);
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getQuote()
    {
        return quote;
    }

    public void setQuote(String quote)
    {
        this.quote = quote;
    }

    public Date getJavaDate()
    {
        return javaDate;
    }

    public void setJavaDate(Date javaDate)
    {
        this.javaDate = javaDate;
    }

    public DateTime getJodaTime()
    {
        return jodaTime;
    }

    public void setJodaTime(DateTime jodaTime)
    {
        this.jodaTime = jodaTime;
    }

}
