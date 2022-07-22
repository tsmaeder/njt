package org.eclipse.njdt.indexer.query;

import java.util.List;

import org.eclipse.njdt.indexer.query.expression.Expression;

public interface IndexReader {
	<T extends Reference<?>> List<T> queryDeclarations(Class<T> what, Expression<Boolean> where);
	<T extends Reference<?>> List<T> queryReferences(Class<T> what, Expression<Boolean> where);
}
