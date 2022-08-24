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

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

/**
 * An interface for looking up attached source for binary types
 * @author Thomas Mäder
 *
 */
public interface SourceLookup {
	void open();
	void close();
	ICompilationUnit lookup(String module, String binaryTypeName);
}
