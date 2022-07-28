package org.eclipse.njdt.indexer.query.expression;

public class StringPredicate extends BinaryOperation<StringPredicate.Operator, String, String, Boolean> {
	public enum Operator {
		Like
	}
	
	public StringPredicate(Expression<String> left, Expression<String> right, Operator operator) {
		super(Boolean.class, left, right, operator);
	}
	
	public <V> V accept(ExpressionVisitor<V> visitor) {
		return visitor.visit(this);
	}
}
