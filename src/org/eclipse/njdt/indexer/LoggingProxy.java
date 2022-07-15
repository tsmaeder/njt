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
