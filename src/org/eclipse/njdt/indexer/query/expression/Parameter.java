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

public class Parameter<T> extends Expression<T> {
	private String name;

	public Parameter(Class<T> type, String name) {
		super(type);
		this.name= name;
	}
	
	public String getName() {
		return name;
	}
	
	public <V> V accept(ExpressionVisitor<V> visitor) {
		return visitor.visit(this);
	}
}
