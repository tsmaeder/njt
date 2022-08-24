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

import org.eclipse.njdt.indexer.FieldReferenceKind;
import org.eclipse.njdt.indexer.MethodReferenceKind;
import org.eclipse.njdt.indexer.MonikerFactory;
import org.eclipse.njdt.indexer.Range;
import org.eclipse.njdt.indexer.TypeReferenceKind;

/**
 * A session object for writing the contents of a single document to the index.
 * clients are expected to invoke {@link IndexWriterDocumentSession#done()}} after
 * indexing a document and before beginning to index the next one.
 */
public interface IndexWriterDocumentSession {
	/**
	 * Add an index entry for a type declaration.
	 * @param modifiers access modifiers, see class file spec
	 * @param module the module name
	 * @param qualifiedTypeName the type name, as it appears in a class file
	 * @param sourceRange the source range of the declaration. What this includes is up to the parser. May be null
	 */
	void addTypeDeclaration(int modifiers, CharSequence module, CharSequence qualifiedTypeName, Range sourceRange);
	void addMethodDeclaration(int modifiers, CharSequence module, CharSequence qualifiedTypeName, CharSequence methodName, CharSequence signature, Range sourceRange);
	void addFieldDeclaration(int modifiers, CharSequence module, CharSequence qualifiedTypeName, CharSequence fieldName, CharSequence fieldTypeMoniker, Range sourceRange);
	
	/**
	 * Add a reference to a type to the index
	 * @param kind
	 * @param typeMoniker the moniker ({@link MonikerFactory} of this type
	 * @param onDemand whether this reference was created via a "import foo.bar.*" import or not: 
	 * 			if it was, the referenced type may change based on the environment (different version of a jar).
	 * @param sourceRange the range of the reference, may be null (maybe this should include all source range instead of just one)
	 */
	void addTypeReference(TypeReferenceKind kind, CharSequence typeMoniker, boolean onDemand, Range sourceRange);
	void addMethodReference(MethodReferenceKind kind, CharSequence typeMoniker, boolean onDemand, CharSequence methodName, CharSequence signature, Range sourceRange);
	void addFieldReference(FieldReferenceKind kind, CharSequence typeMoniker, boolean onDemand, CharSequence fieldName, Range sourceRange);

	/**
	 * The client tells us it's done indexing the document. This object will not be used afterwards, so this
	 * is the last change to persist any changes to the index.
	 */
	void done();
}
