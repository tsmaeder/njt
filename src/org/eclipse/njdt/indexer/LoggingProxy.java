/*******************************************************************************
 * Copyright (c) 2022 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.njdt.indexer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggingProxy implements InvocationHandler {
	private Object wrapped;

	public LoggingProxy(Object wrapped) {
		this.wrapped= wrapped;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		StringBuilder builder= new StringBuilder(method.getName());
		char sep= '(';
		for (Object object : args) {
			builder.append(sep);
			sep= ',';
			builder.append(String.valueOf(object));
		}
		builder.append(')');
		System.out.println(builder);
		if (wrapped != null) {
			return method.invoke(wrapped, args);
		} else {
			return null;
		}
	}

}
