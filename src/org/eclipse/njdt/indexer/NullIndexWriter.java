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
import org.eclipse.njdt.indexer.writer.IndexWriter;
import org.eclipse.njdt.indexer.writer.IndexWriterDocumentSession;

public class NullIndexWriter implements IndexWriter {
	private static final IndexWriterDocumentSession NULL_SESSION= new IndexWriterDocumentSession() {

		@Override
		public void addTypeDeclaration(int modifiers, CharSequence module_name, CharSequence qualifiedTypeName, Range sourceRange) {
		}

		@Override
		public void addMethodDeclaration(int modifiers, CharSequence module_name, CharSequence qualifiedTypeName, CharSequence methodName,
				CharSequence signature, Range sourceRange) {
		}

		@Override
		public void addFieldDeclaration(int modifiers, CharSequence module_name, CharSequence qualifiedTypeName, CharSequence fieldName,
				CharSequence fieldTypeMoniker, Range sourceRange) {
		}

		@Override
		public void addTypeReference(TypeReferenceKind kind, CharSequence typeMoniker, boolean isOnDemandReference, Range sourceRange) {
		}

		@Override
		public void addMethodReference(MethodReferenceKind kind, CharSequence typeMoniker, boolean isOnDemandReference, CharSequence methodName,
				CharSequence signature, Range sourceRange) {
		}

		@Override
		public void addFieldReference(FieldReferenceKind kind, CharSequence typeMoniker, boolean isOnDemandReference,
				CharSequence fieldName, Range sourceRange) {
		}

		@Override
		public void done() {
		}
	};

	@Override
	public IndexWriterDocumentSession beginIndexing(DocumentAddress document) {
		return NULL_SESSION;
	}

}
