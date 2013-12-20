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

package org.apache.tapestry5.services.jersey.providers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderSource;

@Provider
public class ValueEncoderSourceParamConverterProvider implements ParamConverterProvider
{

    private final ValueEncoderSource source;

    public ValueEncoderSourceParamConverterProvider(ValueEncoderSource source)
    {
        this.source = source;
    }

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations)
    {
        final ValueEncoder<T> encoder = source.getValueEncoder(rawType);

        if (encoder == null)
        {
            return null;
        }

        return new ParamConverter<T>()
        {
            @Override
            public T fromString(String value)
            {
                return encoder.toValue(value);
            }

            @Override
            public String toString(T value)
            {
                return encoder.toClient(value);
            }
        };
    }

}
