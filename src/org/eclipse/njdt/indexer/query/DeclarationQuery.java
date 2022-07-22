package org.eclipse.njdt.indexer.query;

public interface DeclarationQuery<T extends Declaration> {
	Class<T> what();
	Expression<T> condition();
}
