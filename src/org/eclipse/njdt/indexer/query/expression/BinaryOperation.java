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

public abstract class BinaryOperation<O, L, R, T> extends Expression<T> {
	private Expression<L> left;
	private Expression<R> right;
	private O operator;

	public BinaryOperation(Class<T> type, Expression<L> left, Expression<R> right, O operator) {
		super(type);
		this.left= left;
		this.right= right;
		this.operator= operator;
	}
	
	public Expression<L> getLeft() {
		return left;
	}
	
	public Expression<R> getRight() {
		return right;
	}
	
	public O getOperator() {
		return operator;
	}}
