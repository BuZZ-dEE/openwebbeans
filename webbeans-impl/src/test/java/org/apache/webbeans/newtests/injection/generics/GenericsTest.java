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
package org.apache.webbeans.newtests.injection.generics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.util.TypeLiteral;

import org.apache.webbeans.newtests.AbstractUnitTest;
import org.junit.Ignore;
import org.junit.Test;


public class GenericsTest extends AbstractUnitTest
{

    @Test
    @Ignore("TODO not yet working!")
    public void injectionPoint() throws Exception
    {
        addExtension(new BarVetoExtension());
        startContainer(Foo.class, Bar.class, Baz.class, BazSubclass.class, GenericFactory.class, GenericQualifier.class);

        Bean<Foo> fooBean = getBean(Foo.class);
        Set<InjectionPoint> injectionPoints = fooBean.getInjectionPoints();

        assertEquals(4, injectionPoints.size());

        for (InjectionPoint injectionPoint : injectionPoints)
        {
            if ("baz".equals(injectionPoint.getMember().getName()))
            {
                assertEquals(new TypeLiteral<Baz<String>>() { }.getType(), injectionPoint.getType());
            }
            else if ("a".equals(injectionPoint.getMember().getName()))
            {
                assertEquals(String.class, injectionPoint.getType());
            }
            else if ("bBazList".equals(injectionPoint.getMember().getName()))
            {
                assertEquals(new TypeLiteral<Baz<List<BazSubclass>>>() { }.getType(), injectionPoint.getType());
            }
            else if ("setAArray".equals(injectionPoint.getMember().getName()))
            {
                assertEquals(String[].class, injectionPoint.getType());
            }
            else
            {
                fail("Unexpected injection point");
            }
        }
    }

    @Test
    @Ignore("TODO not yet working!")
    public void testInjected() throws Exception
    {
        addExtension(new BarVetoExtension());
        startContainer(Foo.class, Bar.class, Baz.class, BazSubclass.class, GenericFactory.class, GenericQualifier.class);

        Foo foo = getInstance(Foo.class);
        assertNotNull(foo);
        assertNotNull(foo.getBaz());
        assertNotNull(foo.getAArray());
        assertNotNull(foo.getBBazList());
    }

    @Test
    @Ignore("TODO not yet working!")
    public void observerResolution() throws Exception
    {
        addExtension(new BarVetoExtension());
        startContainer(Foo.class, Bar.class, Baz.class, BazSubclass.class, GenericFactory.class);

        Set<ObserverMethod<? super BazSubclass>> observerMethods = getBeanManager().resolveObserverMethods(new BazSubclass(null));
        assertEquals(observerMethods.size(), 1);
        ObserverMethod<? super Class<BazSubclass>> observerMethod = (ObserverMethod<? super Class<BazSubclass>>)observerMethods.iterator().next();
        assertEquals(observerMethod.getBeanClass(), Foo.class);
        assertEquals(observerMethod.getObservedType(), new TypeLiteral<Baz<String>>() { }.getType());
    }

    @Test
    @Ignore("TODO not yet working!")
    public void testObserver() throws Exception
    {
        addExtension(new BarVetoExtension());
        startContainer(Foo.class, Bar.class, Baz.class, BazSubclass.class, GenericFactory.class);

        Foo foo = getInstance(Foo.class);
        assertNotNull(foo);
        getBeanManager().fireEvent(new BazSubclass(null));
        assertNotNull(foo.getABazEvent());
        assertEquals((String) foo.getAObserverInjectionPoint(), "a produced String");
    }
}
