package org.eclipse.njdt.indexer.query.expression;

public abstract class Expression<T> {
	private Class<T> type;

	public Expression(Class<T> type) {
		this.type= type;
	}
	
	public Class<T> getType() {
		return type;
	}
	
	public abstract <V> V accept(ExpressionVisitor<V> v);
}
