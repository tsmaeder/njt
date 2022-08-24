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

public abstract class Expression<T> {
	private Class<T> type;

	public Expression(Class<T> type) {
		this.type= type;
	}
	
	public Class<T> getType() {
		return type;
	}
	
	public abstract <V> V accept(ExpressionVisitor<V> v);
}
