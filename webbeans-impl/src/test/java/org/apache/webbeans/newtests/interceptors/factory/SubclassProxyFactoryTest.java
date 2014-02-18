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
package org.apache.webbeans.newtests.interceptors.factory;

import org.apache.webbeans.newtests.AbstractUnitTest;
import org.apache.webbeans.newtests.decorators.common.Cow;
import org.apache.webbeans.newtests.interceptors.factory.beans.MyAbstractTestDecorator;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class SubclassProxyFactoryTest extends AbstractUnitTest
{
    @Test
    public void testSubclassProxy() throws Exception
    {
        startContainer();

        Class<? extends MyAbstractTestDecorator> subClass
                = getWebBeansContext().getSubclassProxyFactory().createSubClass(this.getClass().getClassLoader(), MyAbstractTestDecorator.class);
        Assert.assertNotNull(subClass);

        MyAbstractTestDecorator instance = subClass.newInstance();
        Assert.assertNotNull(instance);

        Cow cow = new Cow();
        instance.setDelegate(cow);

        Assert.assertEquals(49, instance.getAge());

        shutDownContainer();
    }
}
