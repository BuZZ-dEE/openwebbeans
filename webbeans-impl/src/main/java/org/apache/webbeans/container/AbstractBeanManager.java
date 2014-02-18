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
package org.apache.webbeans.container;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMember;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.webbeans.component.InjectionTargetBean;
import org.apache.webbeans.component.WebBeansType;
import org.apache.webbeans.component.creation.BeanAttributesBuilder;
import org.apache.webbeans.component.creation.FieldProducerFactory;
import org.apache.webbeans.component.creation.MethodProducerFactory;
import org.apache.webbeans.component.spi.BeanAttributes;
import org.apache.webbeans.component.spi.InjectionTargetFactory;
import org.apache.webbeans.component.spi.ProducerFactory;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.util.AnnotationUtil;

public abstract class AbstractBeanManager implements BeanManager
{

    protected abstract WebBeansContext getWebBeansContext();
    
    public boolean areInterceptorBindingsEquivalent(Annotation annotation1, Annotation annotation2)
    {
        return AnnotationUtil.isCdiAnnotationEqual(annotation1, annotation2);
    }

    public boolean areQualifiersEquivalent(Annotation annotation1, Annotation annotation2)
    {
        return AnnotationUtil.isCdiAnnotationEqual(annotation1, annotation2);
    }

    public int getInterceptorBindingHashCode(Annotation annotation)
    {
        return AnnotationUtil.getCdiAnnotationHashCode(annotation);
    }

    public int getQualifierHashCode(Annotation annotation)
    {
        return AnnotationUtil.getCdiAnnotationHashCode(annotation);
    }

    /**
     * {@inheritDoc}
     */
    public <T> BeanAttributes<T> createBeanAttributes(AnnotatedType<T> type)
    {
        return BeanAttributesBuilder.forContext(getWebBeansContext()).newBeanAttibutes(type).build();
    }

    /**
     * {@inheritDoc}
     */
    public BeanAttributes<?> createBeanAttributes(AnnotatedMember<?> member)
    {
        if (member instanceof AnnotatedField)
        {
            return BeanAttributesBuilder.forContext(getWebBeansContext()).newBeanAttibutes((AnnotatedField<?>)member).build();
        }
        else if (member instanceof AnnotatedMethod)
        {
            return BeanAttributesBuilder.forContext(getWebBeansContext()).newBeanAttibutes((AnnotatedMethod<?>)member).build();
        }
        else
        {
            throw new IllegalArgumentException("Unsupported member type " + member.getClass().getName());
        }
    }

    public InjectionPoint createInjectionPoint(AnnotatedField<?> field)
    {
        return getWebBeansContext().getInjectionPointFactory().buildInjectionPoint(null, field);
    }

    public InjectionPoint createInjectionPoint(AnnotatedParameter<?> parameter)
    {
        return getWebBeansContext().getInjectionPointFactory().buildInjectionPoint(null, parameter);
    }

    public <X> ProducerFactory<X> getProducerFactory(AnnotatedField<? super X> field, Bean<X> bean)
    {
        return new FieldProducerFactory<X>(field, bean, getWebBeansContext());
    }

    public <X> ProducerFactory<X> getProducerFactory(AnnotatedMethod<? super X> method, Bean<X> bean)
    {
        return new MethodProducerFactory<X>(method, bean, getWebBeansContext());
    }
    
    public <X> InjectionTargetFactory<X> getInjectionTargetFactory(AnnotatedType<X> type)
    {
        return new InjectionTargetFactoryImpl<X>(type, getWebBeansContext());
    }

    public <T> Bean<T> createBean(BeanAttributes<T> attributes, Class<T> type, InjectionTargetFactory<T> factory)
    {
        return new InjectionTargetBean<T>(
                getWebBeansContext(),
                WebBeansType.THIRDPARTY,
                getWebBeansContext().getAnnotatedElementFactory().newAnnotatedType(type),
                attributes,
                type,
                factory);
    }

    public <T, X> Bean<T> createBean(BeanAttributes<T> attributes, Class<X> type, ProducerFactory<X> factory)
    {
        return null;
        //return new AbstractProducerBean<T>(type, getWebBeansContext(), WebBeansType.THIRDPARTY, attributes, returnType, factory);
    }

    public <T extends Extension> T getExtension(Class<T> type)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
