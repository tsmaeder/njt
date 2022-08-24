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

import org.eclipse.jdt.internal.compiler.lookup.ModuleBinding;

/**
 * Determine rules for looking up types in modules based on compiler constants for module names
 * @author Thomas Mäder
 *
 */
public enum ModuleMatchingStrategy {
	Named, Unnamed, AnyNamed, Any;
	
	public static ModuleMatchingStrategy fromModuleName(char[] name) {
		if (name == ModuleBinding.ANY) {
			return Any;
		} else if (name == ModuleBinding.UNNAMED) {
			return Unnamed;
		} else if (name == ModuleBinding.ANY_NAMED) {
			return AnyNamed;
		} else {
			return Named;
		}
	}
}
