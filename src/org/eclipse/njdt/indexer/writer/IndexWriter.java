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
package org.eclipse.njdt.indexer.writer;

/**
 * The entry point for writing to a particular index implementation.
 * @author Thomas Mäder
 *
 */
public interface IndexWriter {
	/**
	 * Create a session object for indexing the given document.
	 * @param document
	 * @return
	 */
	IndexWriterDocumentSession beginIndexing(DocumentAddress document);
}
