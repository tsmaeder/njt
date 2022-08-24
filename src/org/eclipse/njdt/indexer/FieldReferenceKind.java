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
 * Enum for different kinds of references to fields.
 * @author Thomas Mäder
 *
 */
public enum FieldReferenceKind implements IntValue {
	
	None(20),
	Read(21),
	Write(22),
	Both(23);

	public static final int MIN= 20;
	public static final int MAX= 23;

	private int value;

	FieldReferenceKind(int i) {
		this.value= i;
	}

	@Override
	public int getValue() {
		return value;
	}
	
	static FieldReferenceKind forValue(int value) {
		return values()[value-None.getValue()];
	}
}
