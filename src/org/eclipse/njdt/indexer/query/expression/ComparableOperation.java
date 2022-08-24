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

public class ComparableOperation<T extends Comparable<T>> extends BinaryOperation<ComparableOperation.Operator, T, T, Boolean> {
	public enum Operator {
		Less,
		Equal,
		Greater
	}
	
	public ComparableOperation(Expression<T> left, Expression<T> right, Operator operator) {
		super(Boolean.class, left, right, operator);
	}

	public <V> V accept(ExpressionVisitor<V> visitor) {
		return visitor.visit(this);
	}
}
