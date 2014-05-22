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
package org.apache.webbeans.newtests.interceptors.factory.beans;

import org.apache.webbeans.test.component.intercept.webbeans.bindings.Transactional;
import org.apache.webbeans.test.component.intercept.webbeans.bindings.Secure;

/**
 * A simple class which is not intercepted but has some
 * methods which are.
 */
public class MethodInterceptedClass
{
    private boolean defaultCtInvoked = false;

    private int meaningOfLife;

    public MethodInterceptedClass()
    {
        defaultCtInvoked = true;
    }

    @Transactional
    public int getMeaningOfLife()
    {
        System.out.println("answering the question about life, the universe and everything!");
        System.out.println("and being in " + this.getClass());
        return meaningOfLife;
    }

    @Transactional
    @Secure
    public void setMeaningOfLife(int meaningOfLife)
    {
        this.meaningOfLife = meaningOfLife;
    }

    public int nonTransactionalGetter()
    {
        return meaningOfLife + 2;
    }
}
