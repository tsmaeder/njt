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

public class ExpressionVisitor<T> {
	public T visit(ArithmeticOperation e) {
		return null;
	}
	
	public T visit(BooleanOperation e) {
		return null;
	}
	
	public T visit(ComparableOperation<?> e) {
		return null;
	}
	
	public T visit(StringPredicate e) {
		return null;
	}
	
	public T visit(Literal<?> e) {
		return null;
	}
	
	public T visit(Parameter<?> e) {
		return null;
	}
	
	public T visit(Property<?> e) {
		return null;
	}
}
