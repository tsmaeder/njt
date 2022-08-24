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

import org.eclipse.njdt.indexer.writer.DocumentAddress;

/**
 * A factory to build identifiers for type references. A moniker identifies a type either inside the same
 * indexed location or outside of it. If the moniker is non-local, it needs to be resolved against the 
 * resolved location it resides in. See also 
 * https://microsoft.github.io/language-server-protocol/specifications/lsif/0.4.0/specification/#exportsImports
 * @author Thomas Mäder
 *
 */
public interface MonikerFactory {
	CharSequence createTypeMoniker(DocumentAddress referenceFrom, CharSequence typeName);
}
