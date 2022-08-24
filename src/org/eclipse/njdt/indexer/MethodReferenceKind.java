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

/**
 * Enum designating different kinds of references to methods in the index.
 * @author Thomas Mäder
 *
 */
public enum MethodReferenceKind implements IntValue {
	ThisReference(30),
	ImplicitThisReference(31),
	SuperReference(32),
	QualifiedReference(33),
	MethodLiteral(34);
	
	public static final int MIN= 30;
	public static final int MAX= 34;

	private int value;

	MethodReferenceKind(int i) {
		this.value= i;
	}

	@Override
	public int getValue() {
		return value;
	}

	static MethodReferenceKind forValue(int value) {
		return values()[value-ThisReference.getValue()];
	}
}
