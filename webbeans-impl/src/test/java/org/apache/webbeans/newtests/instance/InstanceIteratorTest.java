/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.newtests.instance;

import org.apache.webbeans.newtests.AbstractUnitTest;
import org.junit.Test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import junit.framework.Assert;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class InstanceIteratorTest extends AbstractUnitTest
{
    @Test
    public void testInstanceIteratorWithBeanSelector() {
        startContainer(
                Qualifier1.class,
                Qualifier2.class,
                ShardContract.class,
                Bean1.class,
                Bean2.class,
                BeanSelector.class,
                InstanceHolder.class);

        InstanceHolder instanceHolder = getInstance(InstanceHolder.class);
        assertNotNull(instanceHolder);

        Instance<ShardContract> instance = instanceHolder.getInstance();

        int count = 0;

        for (ShardContract ignored : instance)
        {
            count++;
        }
        assertEquals(3, count); //contextual instances: Bean1, Bean2, 2nd instance of Bean1 exposed by the producer
    }

    @Test
    public void testInstanceWithoutImpl()
    {
        startContainer(InstanceIteratorHolder.class, Bean1.class);

        InstanceIteratorHolder instanceIteratorHolder = getInstance(InstanceIteratorHolder.class);
        assertNotNull(instanceIteratorHolder);

        Assert.assertTrue(instanceIteratorHolder.iterateOverContracts());
    }

    public static class InstanceHolder
    {
        @Inject
        @Any
        private Instance<ShardContract> instance;


        public Instance<ShardContract> getInstance()
        {
            return instance;
        }
    }

    @RequestScoped
    public static class InstanceIteratorHolder
    {
        @Inject
        @Any
        private Instance<ShardContract> instances;

        public boolean iterateOverContracts()
        {
            boolean foundSomething = false;
            for (ShardContract contract: instances)
            {
                foundSomething = true;
            }

            return foundSomething;
        }
    }

    @Target({TYPE, METHOD, PARAMETER})
    @Retention(RUNTIME)
    @Qualifier
    public @interface Qualifier1
    {
    }

    @Target({TYPE, METHOD, PARAMETER})
    @Retention(RUNTIME)
    @Qualifier
    public @interface Qualifier2
    {
    }

    public interface ShardContract
    {
    }

    @ApplicationScoped
    @Qualifier1
    public static class Bean1 implements ShardContract
    {
    }

    @ApplicationScoped
    @Qualifier2
    public static class Bean2 implements ShardContract
    {
    }

    public static class BeanSelector
    {
        @Produces
        protected ShardContract selectBean(@Qualifier1 ShardContract bean)
        {
            return bean; //usually there are different beans -> one gets selected, however, it isn't needed for the test
        }
    }
}
