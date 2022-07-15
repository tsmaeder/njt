package org.eclipse.njdt.indexer.writer;

public interface IndexWriter {
	Object beginIndexing(DocumentAddress document);
	void doneIndexing(Object handle);
	
	void addTypeDeclaration(Object handle, int modifiers, CharSequence qualifiedTypeName, Range sourceRange);
	void addTypeReference(Object handle, TypeReferenceKind kind, CharSequence typeMoniker, Range sourceRange);
	
	void addMethodDeclaration(Object handle, CharSequence qualifiedTypeName, int modifiers, CharSequence methodName, CharSequence signature, Range sourceRange);
	void addMethodReference(Object handle, MethodReferenceKind kind, CharSequence typeMoniker, CharSequence methodName, CharSequence signature, Range sourceRange);
	
	void addFieldDeclaration(Object handle, CharSequence qualifiedTypeName, int modifiers, CharSequence fieldName, CharSequence fieldTypeMoniker);
	void addFieldReference(Object handle, boolean isWriteAccess, boolean isReadAccess, CharSequence typeMoniker, CharSequence fieldName, Range sourceRange);
}
