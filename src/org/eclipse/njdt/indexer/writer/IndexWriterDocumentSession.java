package org.eclipse.njdt.indexer.writer;

public interface IndexWriterDocumentSession {
	void addTypeDeclaration(int modifiers, CharSequence qualifiedTypeName, Range sourceRange);
	void addMethodDeclaration(int modifiers, CharSequence qualifiedTypeName, CharSequence methodName, CharSequence signature, Range sourceRange);
	void addFieldDeclaration(int modifiers, CharSequence qualifiedTypeName, CharSequence fieldName, CharSequence fieldTypeMoniker, Range sourceRange);
	
	void addTypeReference(TypeReferenceKind kind, CharSequence typeMoniker, boolean onDemand, Range sourceRange);
	void addMethodReference(MethodReferenceKind kind, CharSequence typeMoniker, boolean onDemand, CharSequence methodName, CharSequence signature, Range sourceRange);
	void addFieldReference(FieldReferenceKind kind, CharSequence typeMoniker, boolean onDemand, CharSequence fieldName, Range sourceRange);

	void done();
}
