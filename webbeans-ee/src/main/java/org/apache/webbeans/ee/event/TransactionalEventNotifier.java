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
package org.apache.webbeans.ee.event;

import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.spi.EventMetadata;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;

import org.apache.webbeans.config.OWBLogConst;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.event.OwbObserverMethod;
import org.apache.webbeans.logger.WebBeansLoggerFacade;
import org.apache.webbeans.spi.TransactionService;

import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
public final class TransactionalEventNotifier
{
    private static final Logger logger = WebBeansLoggerFacade.getLogger(TransactionalEventNotifier.class);

    private TransactionalEventNotifier()
    {
        // utility class ct
    }

    /**
     * This will get called by the EJB integration code
     */
    public static void registerTransactionSynchronization(TransactionPhase phase, ObserverMethod<? super Object> observer, Object event, EventMetadata metadata) throws Exception
    {
        TransactionService transactionService = WebBeansContext.currentInstance().getService(TransactionService.class);
        
        Transaction transaction = null;
        if(transactionService != null)
        {
            transaction = transactionService.getTransaction();
        }
        
        if(transaction != null)
        {
            if (phase.equals(TransactionPhase.AFTER_COMPLETION))
            {
                transaction.registerSynchronization(new AfterCompletion(observer, event, metadata));
            }
            else if (phase.equals(TransactionPhase.AFTER_SUCCESS))
            {
                transaction.registerSynchronization(new AfterCompletionSuccess(observer, event, metadata));
            }
            else if (phase.equals(TransactionPhase.AFTER_FAILURE))
            {
                transaction.registerSynchronization(new AfterCompletionFailure(observer, event, metadata));
            }
            else if (phase.equals(TransactionPhase.BEFORE_COMPLETION))
            {
                transaction.registerSynchronization(new BeforeCompletion(observer, event, metadata));
            }
            else
            {
                throw new IllegalStateException(WebBeansLoggerFacade.getTokenString(OWBLogConst.EXCEPT_0007) + phase);
            }            
        }
        else
        {
            if (observer instanceof OwbObserverMethod)
            {
                ((OwbObserverMethod<? super Object>)observer).notify(event, metadata);
            }
            else
            {
                observer.notify(event);
            }
        }
    }
    
    private static class AbstractSynchronization<T> implements Synchronization
    {

        private final ObserverMethod<T> observer;
        private final T event;
        private final EventMetadata metadata;

        public AbstractSynchronization(ObserverMethod<T> observer, T event, EventMetadata metadata)
        {
            this.observer = observer;
            this.event = event;
            this.metadata = metadata;
        }

        @Override
        public void beforeCompletion()
        {
            // Do nothing
        }

        @Override
        public void afterCompletion(int i)
        {
            //Do nothing
        }

        public void notifyObserver()
        {
            try
            {
                if (observer instanceof OwbObserverMethod)
                {
                    ((OwbObserverMethod<T>)observer).notify(event, metadata);
                }
                else
                {
                    observer.notify(event);
                }
            }
            catch (Exception e)
            {
                logger.log(Level.SEVERE, OWBLogConst.ERROR_0003, e);
            }
        }
    }

    private static final class BeforeCompletion extends AbstractSynchronization
    {
        private BeforeCompletion(ObserverMethod observer, Object event, EventMetadata metadata)
        {
            super(observer, event, metadata);
        }

        @Override
        public void beforeCompletion()
        {
            notifyObserver();
        }
    }

    private static final class AfterCompletion extends AbstractSynchronization
    {
        private AfterCompletion(ObserverMethod observer, Object event, EventMetadata metadata)
        {
            super(observer, event, metadata);
        }

        @Override
        public void afterCompletion(int i)
        {
            notifyObserver();
        }
    }

    private static final class AfterCompletionSuccess extends AbstractSynchronization
    {
        private AfterCompletionSuccess(ObserverMethod observer, Object event, EventMetadata metadata)
        {
            super(observer, event, metadata);
        }

        @Override
        public void afterCompletion(int i)
        {
            if (i == Status.STATUS_COMMITTED)
            {
                notifyObserver();
            }
        }
    }

    private static final class AfterCompletionFailure extends AbstractSynchronization
    {
        private AfterCompletionFailure(ObserverMethod observer, Object event, EventMetadata metadata)
        {
            super(observer, event, metadata);
        }

        @Override
        public void afterCompletion(int i)
        {
            if (i != Status.STATUS_COMMITTED)
            {
                notifyObserver();
            }
        }
    }
    
}
