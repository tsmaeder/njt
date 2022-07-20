package org.eclipse.njdt.indexer;

import org.eclipse.njdt.indexer.writer.DocumentAddress;
import org.eclipse.njdt.indexer.writer.FieldReferenceKind;
import org.eclipse.njdt.indexer.writer.IndexWriter;
import org.eclipse.njdt.indexer.writer.IndexWriterDocumentSession;
import org.eclipse.njdt.indexer.writer.MethodReferenceKind;
import org.eclipse.njdt.indexer.writer.Range;
import org.eclipse.njdt.indexer.writer.TypeReferenceKind;

public class NullIndexWriter implements IndexWriter {
	private static final IndexWriterDocumentSession NULL_SESSION= new IndexWriterDocumentSession() {

		@Override
		public void addTypeDeclaration(int modifiers, CharSequence qualifiedTypeName, Range sourceRange) {
		}

		@Override
		public void addMethodDeclaration(int modifiers, CharSequence qualifiedTypeName, CharSequence methodName,
				CharSequence signature, Range sourceRange) {
		}

		@Override
		public void addFieldDeclaration(int modifiers, CharSequence qualifiedTypeName, CharSequence fieldName,
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
