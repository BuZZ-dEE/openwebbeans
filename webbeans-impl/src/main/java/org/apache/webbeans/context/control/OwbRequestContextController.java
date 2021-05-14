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

package org.apache.webbeans.context.control;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.intercept.RequestScopedBeanInterceptorHandler;
import org.apache.webbeans.spi.ContextsService;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.context.spi.Context;
import java.util.ArrayList;
import java.util.List;

public class OwbRequestContextController implements RequestContextController
{
    private final ContextsService contextsService;
    private final ThreadLocal<List<Op>> deactivateOperations;

    OwbRequestContextController(final WebBeansContext context)
    {
        this.contextsService = context.getContextsService();
        this.deactivateOperations = findThreadLocal(context);
    }

    @Override
    public boolean activate()
    {
        final Context ctx = contextsService.getCurrentContext(RequestScoped.class, false);
        if (ctx == null || !ctx.isActive())
        {
            contextsService.startContext(RequestScoped.class, null);
            final List<Op> ops = new ArrayList<>();
            ops.add(Op.DEACTIVATE);
            deactivateOperations.set(ops);
            return true;
        }
        List<Op> deactivateOps = deactivateOperations.get();
        if (deactivateOps == null)
        {
            deactivateOps = new ArrayList<>();
            deactivateOperations.set(deactivateOps);
        }
        deactivateOps.add(Op.NOOP);
        return false;
    }

    @Override
    public void deactivate() throws ContextNotActiveException
    {
        // spec says we only must deactivate the RequestContest "if it was activated by this context controller"
        final List<Op> ops = deactivateOperations.get();
        if (ops == null)
        {
            return;
        }
        if (ops.remove(ops.size() - 1) == Op.DEACTIVATE)
        {
            contextsService.endContext(RequestScoped.class, null);
            RequestScopedBeanInterceptorHandler.removeThreadLocals();
        }
        if (ops.isEmpty())
        {
            deactivateOperations.remove();
        }
    }

    // must be per webbeanscontext
    private ThreadLocal<List<Op>> findThreadLocal(final WebBeansContext context)
    {
        ThreadLocalService service = context.getService(ThreadLocalService.class);
        if (service == null)
        {
            synchronized (context)
            {
                if (service == null)
                {
                    service = new ThreadLocalService();
                    context.registerService(ThreadLocalService.class, service);
                }
            }
        }
        return service.instance;
    }

    private enum Op
    {
        DEACTIVATE, NOOP
    }

    private static class ThreadLocalService
    {
        private final ThreadLocal<List<Op>> instance = new ThreadLocal<>();
    }
}
