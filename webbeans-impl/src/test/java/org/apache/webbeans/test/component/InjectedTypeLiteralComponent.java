/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under the License.
 */
package org.apache.webbeans.test.component;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.apache.webbeans.annotation.deployment.Production;

@Production
@RequestScoped
public class InjectedTypeLiteralComponent
{
    private @Inject @Default ITypeLiteralComponent<List<String>> component;
    
    private @Inject @Default ITypeLiteralComponent eraseComponent;

    public InjectedTypeLiteralComponent()
    {
        super();
    }

    /**
     * @return the component
     */
    public ITypeLiteralComponent<List<String>> getComponent()
    {
        return component;
    }

    /**
     * @param component the component to set
     */
    public void setComponent(ITypeLiteralComponent<List<String>> component)
    {
        this.component = component;
    }

    /**
     * @return the eraseComponent
     */
    public ITypeLiteralComponent getEraseComponent()
    {
        return eraseComponent;
    }

    /**
     * @param eraseComponent the eraseComponent to set
     */
    public void setEraseComponent(ITypeLiteralComponent eraseComponent)
    {
        this.eraseComponent = eraseComponent;
    }

}
