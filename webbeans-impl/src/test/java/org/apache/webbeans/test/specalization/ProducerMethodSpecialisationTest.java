/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.test.specalization;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.webbeans.test.AbstractUnitTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProducerMethodSpecialisationTest extends AbstractUnitTest
{
    @Test
    public void testProducerMethodSpecialisation()
    {
        startContainer(A.class, B.class);

        assertEquals("B-one", getInstance(String.class, new AnnotationLiteral<OneProducesMethod>(){}));
        assertEquals("B-two", getInstance(String.class, new AnnotationLiteral<TwoProducesMethod>(){}));

        assertEquals("A-three", getInstance(String.class, new AnnotationLiteral<ThreeProducesMethod>(){}));
    }


    public static class A 
    {
        @Produces
        @OneProducesMethod
        public String getX() 
        {
            return "A-one";
        }

        @Produces
        @TwoProducesMethod
        public String getY()
        {
            return "A-two";
        }

        @Produces
        @ThreeProducesMethod
        public String getZ()
        {
            return "A-three";
        }
    }

    public static class B extends A 
    {
        @Produces
        @Specializes
        @OneProducesMethod
        public String getX()
        {
            return "B-one";
        }

        @Produces
        @Specializes
        @TwoProducesMethod
        public String getY()
        {
            return "B-two";
        }
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Qualifier
    public @interface OneProducesMethod 
    {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Qualifier
    public @interface TwoProducesMethod
    {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Qualifier
    public @interface ThreeProducesMethod
    {
    }

}
