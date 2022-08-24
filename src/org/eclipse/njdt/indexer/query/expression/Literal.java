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
package org.eclipse.njdt.indexer.query.expression;

public class Literal<T> extends Expression<T> {
	private T value;
	
	public Literal(Class<T> type, T value) {
		super(type);
		this.value= value;
	}

	public T getValue() {
		return value;
	}
	
	public <V> V accept(ExpressionVisitor<V> visitor) {
		return visitor.visit(this);
	}
}
