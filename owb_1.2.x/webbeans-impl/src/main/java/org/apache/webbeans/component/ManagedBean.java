/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.component;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.PassivationCapable;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.context.creational.CreationalContextImpl;

/**
 * Managed bean implementation of the {@link javax.enterprise.inject.spi.Bean}.
 * 
 * @version $Rev$ $Date$
 */
public class ManagedBean<T> extends InjectionTargetBean<T> implements InterceptedMarker, PassivationCapable
{    
    public ManagedBean(WebBeansContext webBeansContext,
                       WebBeansType webBeansType,
                       AnnotatedType<T> annotated,
                       BeanAttributesImpl<T> beanAttributes,
                       Class<T> beanClass)
    {
        super(webBeansContext, webBeansType, annotated, beanAttributes, beanClass);
    }

    public T create(CreationalContext<T> creationalContext)
    {
        if (!(creationalContext instanceof CreationalContextImpl))
        {
            creationalContext = webBeansContext.getCreationalContextFactory().wrappedCreationalContext(creationalContext, this);
        }
        CreationalContextImpl<T> creationalContextImpl = (CreationalContextImpl<T>)creationalContext;
        Bean<T> oldBean = creationalContextImpl.putBean(this);
        try
        {
            return super.create(creationalContext);
        }
        finally
        {
            creationalContextImpl.putBean(oldBean);
        }
    }
}
