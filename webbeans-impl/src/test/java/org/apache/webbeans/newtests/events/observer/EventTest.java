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
package org.apache.webbeans.newtests.events.observer;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.webbeans.newtests.AbstractUnitTest;
import org.junit.Assert;
import org.junit.Test;

public class EventTest extends AbstractUnitTest {

    @Test
    public void multipleObserverMethodsWithSameName()
    {
        Collection<Class<?>> beanClasses = new ArrayList<Class<?>>();
        beanClasses.add(Painter.class);
        startContainer(beanClasses, null);

        final Orange orange = new Orange();
        getBeanManager().fireEvent(orange);

        final Green green = new Green();
        getBeanManager().fireEvent(green);

        final Painter painter = getInstance(Painter.class);
        Assert.assertEquals(2, painter.getObserved().size());
        Assert.assertSame(orange, painter.getObserved().get(0));
        Assert.assertSame(green, painter.getObserved().get(1));

        shutDownContainer();
    }

    @Test
    public void testOverriddenObserverMethodsInSubclasses()
    {
        Collection<Class<?>> beanClasses = new ArrayList<Class<?>>();
        beanClasses.add(Superclass.class);
        beanClasses.add(BeanA.class);
        startContainer(beanClasses, null);

        TestEvent testEvent = new TestEvent();
        getBeanManager().fireEvent(testEvent);

        Assert.assertEquals(1, testEvent.getCalledObservers().size());
        Assert.assertTrue(testEvent.getCalledObservers().iterator().next().equals("BeanA"));

        shutDownContainer();
    }

    @Test
    public void testSubclassRemovesObserverAnnotationByOverriding()
    {
        Collection<Class<?>> beanClasses = new ArrayList<Class<?>>();
        beanClasses.add(Superclass.class);
        beanClasses.add(BeanB.class);
        startContainer(beanClasses, null);

        TestEvent testEvent = new TestEvent();
        getBeanManager().fireEvent(testEvent);

        Assert.assertEquals(0, testEvent.getCalledObservers().size());

        shutDownContainer();
    }
    
    @Test
    public void testObserverOnPrivateMethod() {
        Collection<Class<?>> beanClasses = new ArrayList<Class<?>>();
        beanClasses.add(Superclass.class);
        beanClasses.add(BeanA.class);
        startContainer(beanClasses, null);

        PrivateTestEvent testEvent = new PrivateTestEvent();
        getBeanManager().fireEvent(testEvent);

        Assert.assertEquals(2, testEvent.getCalledObservers().size());
        Assert.assertTrue(testEvent.getCalledObservers().contains("BeanA"));
        Assert.assertTrue(testEvent.getCalledObservers().contains("BeanA[Superclass]"));

        shutDownContainer();

    }
    
    @Test
    public void testPrivateMethodCannotBeOverridden() {
        Collection<Class<?>> beanClasses = new ArrayList<Class<?>>();
        beanClasses.add(Superclass.class);
        beanClasses.add(BeanB.class);
        startContainer(beanClasses, null);

        PrivateTestEvent testEvent = new PrivateTestEvent();
        getBeanManager().fireEvent(testEvent);

        Assert.assertEquals(1, testEvent.getCalledObservers().size());
        Assert.assertEquals("BeanB[Superclass]", testEvent.getCalledObservers().iterator().next());

        shutDownContainer();

    }
    
    @Test
    public void testObserverOnStaticMethod() {
        Collection<Class<?>> beanClasses = new ArrayList<Class<?>>();
        beanClasses.add(Superclass.class);
        beanClasses.add(BeanA.class);
        startContainer(beanClasses, null);

        StaticTestEvent testEvent = new StaticTestEvent();
        getBeanManager().fireEvent(testEvent);

        Assert.assertEquals(2, testEvent.getCalledObservers().size());
        Assert.assertTrue(testEvent.getCalledObservers().contains("BeanA"));
        Assert.assertTrue(testEvent.getCalledObservers().contains("Superclass"));

        shutDownContainer();

    }
    
    @Test
    public void testStaticMethodCannotBeOverridden() {
        Collection<Class<?>> beanClasses = new ArrayList<Class<?>>();
        beanClasses.add(Superclass.class);
        beanClasses.add(BeanB.class);
        startContainer(beanClasses, null);

        StaticTestEvent testEvent = new StaticTestEvent();
        getBeanManager().fireEvent(testEvent);

        Assert.assertEquals(1, testEvent.getCalledObservers().size());
        Assert.assertEquals("Superclass", testEvent.getCalledObservers().iterator().next());

        shutDownContainer();

    }
}
