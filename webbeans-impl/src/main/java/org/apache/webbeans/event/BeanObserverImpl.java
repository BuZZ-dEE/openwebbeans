/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.webbeans.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.Context;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.ObserverMethod;

import org.apache.webbeans.annotation.DefaultLiteral;
import org.apache.webbeans.component.AbstractBean;
import org.apache.webbeans.component.InjectionTargetBean;
import org.apache.webbeans.container.BeanManagerImpl;
import org.apache.webbeans.container.InjectionResolver;
import org.apache.webbeans.container.activity.ActivityManager;
import org.apache.webbeans.exception.WebBeansException;
import org.apache.webbeans.logger.WebBeansLogger;
import org.apache.webbeans.util.AnnotationUtil;
import org.apache.webbeans.util.WebBeansUtil;

/**
 * Defines observers that are declared in observer methods.
 * <p>
 * Example:
 * <pre>
 *  public class X {
 *      
 *      public void afterLoggedIn(@Observes @Current LoggedInEvent event)
 *      {
 *          .....
 *      }
 *  }
 * </pre>
 * Above class X instance observes for the event with type <code>LoggedInEvent</code>
 * and event qualifier is <code>Current</code>. Whenever event is fired, its {@link Observer#notify()}
 * method is called.
 * </p>
 * 
 * @version $Rev$ $Date$
 *
 * @param <T> event type
 */
public class BeanObserverImpl<T> implements ObserverMethod<T>
{
    /**Logger instance*/
    private static final WebBeansLogger logger = WebBeansLogger.getLogger(BeanObserverImpl.class);

    /**Observer owner bean that defines observer method*/
    private final InjectionTargetBean<?> bean;

    /**Event observer method*/
    private final Method observerMethod;

    /**Using existing bean instance or not*/
    private final boolean ifExist;

    /**
     * Creates a new bean observer instance.
     * 
     * @param bean owner
     * @param observerMethod method
     * @param ifExist if exist parameter
     * @param type transaction type
     */
    public BeanObserverImpl(InjectionTargetBean<?> bean, Method observerMethod, boolean ifExist)
    {
        this.bean = bean;
        this.observerMethod = observerMethod;
        this.ifExist = ifExist;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void notify(T event)
    {
        AbstractBean<Object> baseComponent = (AbstractBean<Object>) bean;
        AbstractBean<Object> specializedComponent = null;
        Object object = null;

        try
        {
            BeanManagerImpl manager = ActivityManager.getInstance().getCurrentActivity();
            specializedComponent = (AbstractBean<Object>)WebBeansUtil.getMostSpecializedBean(manager, baseComponent);        
            Context context = manager.getContext(specializedComponent.getScope());
            
            if(this.ifExist)
            {
                object = context.get(specializedComponent);
            }
            else
            {
                object = manager.getInstance(specializedComponent);    
            }
            

            if (this.ifExist && object == null)
            {
                return;
            }

            if (object != null)
            {
                Object[] args = null;
                
                List<Object> argsObjects = getMethodArguments(event);
                args = new Object[argsObjects.size()];
                args = argsObjects.toArray(args);

                if (!this.observerMethod.isAccessible())
                {
                    this.observerMethod.setAccessible(true);
                }

                //Static or not
                if (Modifier.isStatic(this.observerMethod.getModifiers()))
                {
                    object = null;
                }

                //Invoke Method
                this.observerMethod.invoke(object, args);
            }
        }
        catch (Exception e)
        {
                throw new WebBeansException(e);
        }
        finally
        {
            if (baseComponent.getScope().equals(Dependent.class))
            {
                baseComponent.destroy(object,baseComponent.getCreationalContext());
            }
        }

    }

    /**
     * Returns list of observer method parameters.
     * 
     * @param event event instance
     * @return list of observer method parameters
     */
    protected List<Object> getMethodArguments(Object event)
    {
        Type[] types = this.observerMethod.getGenericParameterTypes();

        Annotation[][] annots = this.observerMethod.getParameterAnnotations();

        List<Object> list = new ArrayList<Object>();

        BeanManagerImpl manager = ActivityManager.getInstance().getCurrentActivity();

        if (types.length > 0)
        {
            int i = 0;
            for (Type type : types)
            {
                Annotation[] annot = annots[i];

                boolean observesAnnotation = false;

                if (annot.length == 0)
                {
                    annot = new Annotation[1];
                    annot[0] = new DefaultLiteral();
                }
                else
                {
                    for (Annotation observersAnnot : annot)
                    {
                        if (observersAnnot.annotationType().equals(Observes.class))
                        {
                            list.add(event);
                            observesAnnotation = true;
                            break;
                        }
                    }
                }

                if (!observesAnnotation)
                {
                    //Get parameter annotations
                    Annotation[] bindingTypes = AnnotationUtil.getQualifierAnnotations(annot);

                    if (bindingTypes.length > 0)
                    {
                        list.add(manager.getInstance(InjectionResolver.getInstance().implResolveByType(type, bindingTypes).iterator().next()));
                    }
                    else
                    {
                        list.add(null);
                    }
                }
                
                i++;
            }
        }

        return list;
    }

    /**
     * Returns observer owner bean.
     * 
     * @return the bean
     */
    public Class<?> getBeanClass()
    {
        return bean.getClass();
    }

    public Set<Annotation> getObservedQualifiers() {
        // TODO Auto-generated method stub
        return null;
    }

    public Type getObservedType() {
        // TODO Auto-generated method stub
        return null;
    }

    public Reception getReception() {
        // TODO Auto-generated method stub
        return null;
    }

    public TransactionPhase getTransactionPhase() {
        // TODO Auto-generated method stub
        return null;
    }

}