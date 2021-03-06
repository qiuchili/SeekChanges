/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * {@link ValidationError} related tests
 * 
 * @author igor
 */
public class ValidationErrorTest
{
	/**
	 * tests standard key values
	 */
	@Test
	public void standardKeys()
	{
		ValidationError e = new ValidationError();
		e.addKey(new TestValidator());
		assertEquals("TestValidator", e.getKeys().get(0));

		e = new ValidationError(new TestValidator());
		assertEquals("TestValidator", e.getKeys().get(0));

		e = new ValidationError();
		e.addKey(new TestValidator(), "foo");
		assertEquals("TestValidator.foo", e.getKeys().get(0));

		e = new ValidationError(new TestValidator(), "foo");
		assertEquals("TestValidator.foo", e.getKeys().get(0));

		e = new ValidationError();
		e.addKey(new TestValidator(), null);
		assertEquals("TestValidator", e.getKeys().get(0));

		e = new ValidationError(new TestValidator(), null);
		assertEquals("TestValidator", e.getKeys().get(0));

		e = new ValidationError();
		e.addKey(new TestValidator(), " ");
		assertEquals("TestValidator", e.getKeys().get(0));

		e = new ValidationError(new TestValidator(), " ");
		assertEquals("TestValidator", e.getKeys().get(0));

		e = new ValidationError();
		e.addKey(new TestValidator(), " foo ");
		assertEquals("TestValidator.foo", e.getKeys().get(0));

		e = new ValidationError(new TestValidator(), " foo ");
		assertEquals("TestValidator.foo", e.getKeys().get(0));

	}

	private static class TestValidator implements IValidator<String>
	{

		@Override
		public void validate(IValidatable<String> validatable)
		{
		}

	}
}
