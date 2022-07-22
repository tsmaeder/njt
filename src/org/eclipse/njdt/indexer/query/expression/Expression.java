package org.eclipse.njdt.indexer.query.expression;

public class Expression<T> {
	private Class<T> type;

	public Expression(Class<T> type) {
		this.type= type;
	}
	
	public Class<T> getType() {
		return type;
	}
}
