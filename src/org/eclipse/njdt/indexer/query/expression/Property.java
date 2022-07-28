package org.eclipse.njdt.indexer.query.expression;

public class Property<T> extends Expression<T> {
	private String name;

	public Property(Class<T> type, String name) {
		super(type);
		this.name= name;
	}
	
	public String getName() {
		return name;
	}
	
	public <V> V accept(ExpressionVisitor<V> visitor) {
		return visitor.visit(this);
	}
}
