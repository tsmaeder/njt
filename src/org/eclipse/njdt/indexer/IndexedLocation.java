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

import org.eclipse.njdt.indexer.writer.IndexWriter;

/**
 * An indexed location is a unit that will be indexed in one session.
 * Typically a jar file, a JDK installation or a workspace project
 * @author Thomas Mäder
 *
 */
public interface IndexedLocation {
	/**
	 * 
	 * @param typeName
	 * @return a unique index for the type in the scope of this indexed location 
	 * index id and type id must be unique globally
	 */
	CharSequence lookupTypeId(CharSequence typeName);
	/**
	 * The unique index id of this indexed location. If the index Id is the same, 
	 * the index contents should end up the same and can be reused.
	 * @return
	 */
	String getIndexUri();
	/**
	 * Rebuild the index of this indexed location
	 * @param indexWriter the index writer to use
	 */
	void index(IndexWriter indexWriter);
}
