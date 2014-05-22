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

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;
import javax.enterprise.inject.spi.PassivationCapable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.webbeans.config.WebBeansContext;

/**
 * <p>{@link javax.enterprise.inject.spi.Interceptor} Bean implementation for EJB-style Beans.
 * This is Interceptors which got defined by being referenced via
 * &#064;{@link javax.interceptor.Interceptors}.</p>
 */
public class EjbInterceptorBean<T> extends InterceptorBean<T> implements Interceptor<T>, PassivationCapable
{

    public EjbInterceptorBean(WebBeansContext webBeansContext,
                              AnnotatedType<T> annotated,
                              BeanAttributesImpl<T> beanAttributes,
                              Class<T> beanClass,
                              Map<InterceptionType, Method[]> interceptionMethods,
                              Method aroundConstruct)
    {
        super(webBeansContext, annotated, beanAttributes, beanClass, interceptionMethods, aroundConstruct);
    }

    /**
     * @return always an empty Set as this interceptor doesn't have any InterceptorBindings
     */
    @Override
    public Set<Annotation> getInterceptorBindings()
    {
        return Collections.emptySet();
    }


}
