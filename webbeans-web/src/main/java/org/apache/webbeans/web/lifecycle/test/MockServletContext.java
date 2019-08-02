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
package org.apache.webbeans.web.lifecycle.test;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import static java.util.Collections.enumeration;

/**
 * Implement the ServletContext interface for testing.
 */
public class MockServletContext implements ServletContext
{

    private Map<String, Object> attributes = new HashMap<>();
    private Map<String, String> initParams = new HashMap<>();


    @Override
    public String getContextPath()
    {
        return null;
    }

    @Override
    public ServletContext getContext(String uripath)
    {
        return null;
    }

    @Override
    public int getMajorVersion()
    {
        return 0;
    }

    @Override
    public int getMinorVersion()
    {
        return 0;
    }

    @Override
    public String getMimeType(String file)
    {
        return null;
    }

    @Override
    public Set<String> getResourcePaths(String path)
    {
        return null;
    }

    @Override
    public URL getResource(String path) throws MalformedURLException
    {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String path)
    {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path)
    {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name)
    {
        return null;
    }

    @Override
    public Servlet getServlet(String name) throws ServletException
    {
        return null;
    }

    @Override
    public Enumeration<Servlet> getServlets()
    {
        return null;
    }

    @Override
    public Enumeration<String> getServletNames()
    {
        return null;
    }

    @Override
    public void log(String msg)
    {

    }

    @Override
    public void log(Exception exception, String msg)
    {

    }

    @Override
    public void log(String message, Throwable throwable)
    {

    }

    @Override
    public String getRealPath(String path)
    {
        return null;
    }

    @Override
    public String getServerInfo()
    {
        return null;
    }

    @Override
    public String getInitParameter(String name)
    {
        return initParams.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames()
    {
        return new Enumeration<String>()
        {
            Iterator<String> it = initParams.keySet().iterator();

            @Override
            public boolean hasMoreElements()
            {
                return it.hasNext();
            }

            @Override
            public String nextElement()
            {
                return it.next();
            }
        };
    }

    @Override
    public boolean setInitParameter(String name, String value)
    {
        initParams.put(name, value);
        return true;
    }

    @Override
    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames()
    {
        return enumeration(attributes.keySet());
    }

    @Override
    public void setAttribute(String name, Object object)
    {
        attributes.put(name, object);
    }

    @Override
    public void removeAttribute(String name)
    {

    }

    @Override
    public String getServletContextName()
    {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, String className) throws IllegalArgumentException, IllegalStateException
    {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) throws IllegalArgumentException, IllegalStateException
    {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> clazz) throws IllegalArgumentException, IllegalStateException
    {
        return null;
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException
    {
        return null;
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName)
    {
        return null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations()
    {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) throws IllegalArgumentException, IllegalStateException
    {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) throws IllegalArgumentException, IllegalStateException
    {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) throws IllegalArgumentException, IllegalStateException
    {
        return null;
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException
    {
        return null;
    }

    @Override
    public FilterRegistration getFilterRegistration(String filterName)
    {
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations()
    {
        return null;
    }

    @Override
    public void addListener(Class<? extends EventListener> listenerClass)
    {

    }

    @Override
    public void addListener(String className)
    {

    }

    @Override
    public <T extends EventListener> void addListener(T t)
    {

    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException
    {
        return null;
    }

    @Override
    public void declareRoles(String... roleNames)
    {

    }

    @Override
    public SessionCookieConfig getSessionCookieConfig()
    {
        return null;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes)
    {

    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes()
    {
        return null;
    }

    @Override
    public int getEffectiveMajorVersion() throws UnsupportedOperationException
    {
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() throws UnsupportedOperationException
    {
        return 0;
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes()
    {
        return null;
    }

    @Override
    public ClassLoader getClassLoader()
    {
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor()
    {
        return null;
    }
}
