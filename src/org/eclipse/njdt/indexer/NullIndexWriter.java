package org.eclipse.njdt.indexer;

import org.eclipse.njdt.indexer.writer.DocumentAddress;
import org.eclipse.njdt.indexer.writer.IndexWriter;
import org.eclipse.njdt.indexer.writer.MethodReferenceKind;
import org.eclipse.njdt.indexer.writer.Range;
import org.eclipse.njdt.indexer.writer.TypeReferenceKind;

public class NullIndexWriter implements IndexWriter {

	@Override
	public Object beginIndexing(DocumentAddress document) {
		return null;
	}

	@Override
	public void doneIndexing(Object handle) {
	}

	@Override
	public void addTypeDeclaration(Object handle, int modifiers, CharSequence qualifiedTypeName, Range sourceRange) {
	}

	@Override
	public void addTypeReference(Object handle, TypeReferenceKind kind, CharSequence typeMoniker, Range sourceRange) {
	}

	@Override
	public void addMethodDeclaration(Object handle, CharSequence qualifiedTypeName, int modifiers,
			CharSequence methodName, CharSequence signature, Range sourceRange) {
	}

	@Override
	public void addMethodReference(Object handle, MethodReferenceKind kind, CharSequence typeMoniker,
			CharSequence methodName, CharSequence signature, Range sourceRange) {
	}

	@Override
	public void addFieldDeclaration(Object handle, CharSequence qualifiedTypeName, int modifiers,
			CharSequence fieldName, CharSequence fieldTypeMoniker) {
	}

	@Override
	public void addFieldReference(Object handle, boolean isWriteAccess, boolean isReadAccess, CharSequence typeMoniker,
			CharSequence fieldName, Range sourceRange) {
	}

}
