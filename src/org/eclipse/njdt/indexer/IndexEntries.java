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
 * This file is just served as a thinking aid when designing the index.
 * @author Thomas Mäder
 *
 */
public interface IndexEntries {

}


class Location {
	String locationUri;
	int start;
	int length;
}

class TypeReference {
	String name;
	Location location;
	
	String locationURI; // locationUri= (local | remote:IndexId)path/to/document
	boolean implicitReference;
	String referenceKind; // declaration, cast, extends, implements
}

class MethodReference {
	String name;
	String typeName;
	String locationUri;
	boolean implicit;
	String referenceKind;
}
