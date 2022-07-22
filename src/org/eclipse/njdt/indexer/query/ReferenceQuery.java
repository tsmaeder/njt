package org.eclipse.njdt.indexer.query;

public interface ReferenceQuery<T extends Reference<?>> {
	Class<T> what();
	Expression<T> condition();
}
