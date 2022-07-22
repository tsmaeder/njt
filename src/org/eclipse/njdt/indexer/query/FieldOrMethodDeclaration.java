package org.eclipse.njdt.indexer.query;

public interface FieldOrMethodDeclaration extends Declaration {
	CharSequence name();
	CharSequence signature();
}
