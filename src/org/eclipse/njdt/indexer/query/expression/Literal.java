package org.eclipse.njdt.indexer.query.expression;

public class Literal<T> extends Expression<T> {
	private T value;
	
	public Literal(Class<T> type, T value) {
		super(type);
		this.value= value;
	}

	public T getValue() {
		return value;
	}
	
	public <V> V accept(ExpressionVisitor<V> visitor) {
		return visitor.visit(this);
	}
}
