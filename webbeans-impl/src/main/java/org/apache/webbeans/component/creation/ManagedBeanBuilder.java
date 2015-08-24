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
package org.apache.webbeans.component.creation;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.UnproxyableResolutionException;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanAttributes;

import org.apache.webbeans.component.ManagedBean;
import org.apache.webbeans.component.WebBeansType;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.util.Asserts;
import org.apache.webbeans.util.WebBeansUtil;

/**
 * Bean builder for <i>Managed Beans</i>. A <i>ManagedBean</i> is a class
 * which gets scanned and picked up as {@link javax.enterprise.inject.spi.Bean}.
 * 
 * @version $Rev$ $Date$
 *
 * @param <T> bean type info
 */
public class ManagedBeanBuilder<T, M extends ManagedBean<T>>
{
    protected final WebBeansContext webBeansContext;
    protected final AnnotatedType<T> annotatedType;
    protected final BeanAttributes<T> beanAttributes;

    /**
     * Creates a new creator.
     */
    public ManagedBeanBuilder(WebBeansContext webBeansContext, AnnotatedType<T> annotatedType, BeanAttributes<T> beanAttributes)
    {
        Asserts.assertNotNull(webBeansContext, Asserts.PARAM_NAME_WEBBEANSCONTEXT);
        Asserts.assertNotNull(annotatedType, "annotated type");
        Asserts.assertNotNull(beanAttributes, "beanAttributes");
        this.webBeansContext = webBeansContext;
        this.annotatedType = annotatedType;
        this.beanAttributes = beanAttributes;
    }

    /**
     * {@inheritDoc}
     */
    public M getBean()
    {
        M bean = (M) new ManagedBean<T>(webBeansContext, WebBeansType.MANAGED, annotatedType, beanAttributes, annotatedType.getJavaClass());
        bean.setEnabled(webBeansContext.getWebBeansUtil().isBeanEnabled(annotatedType, bean.getStereotypes()));
        webBeansContext.getWebBeansUtil().checkManagedBeanCondition(annotatedType);
        WebBeansUtil.checkGenericType(annotatedType.getJavaClass(), beanAttributes.getScope());
        webBeansContext.getWebBeansUtil().validateBeanInjection(bean);

        final UnproxyableResolutionException lazyException = webBeansContext.getDeploymentValidationService().validateProxyable(bean);
        if (lazyException == null)
        {
            return bean;
        }
        return (M) new UnproxyableBean<T>(webBeansContext, WebBeansType.MANAGED, beanAttributes, annotatedType, annotatedType.getJavaClass(), lazyException);
    }

    private static class UnproxyableBean<T> extends ManagedBean<T>
    {
        private final UnproxyableResolutionException exception;

        public UnproxyableBean(final WebBeansContext webBeansContext, final WebBeansType webBeansType,
                               final BeanAttributes<T> beanAttributes, final AnnotatedType<T> at, final Class<T> beanClass,
                               final UnproxyableResolutionException error)
        {
            super(webBeansContext, webBeansType, at, beanAttributes, beanClass);
            this.exception = error;
        }

        @Override
        public boolean valid()
        {
            throw exception;
        }

        @Override
        public T create(final CreationalContext<T> creationalContext)
        {
            throw exception;
        }
    }
}
