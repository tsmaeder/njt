package org.eclipse.njdt.indexer.query.expression;

public class BooleanOperation extends BinaryOperation<BooleanOperation.Operator, Boolean, Boolean, Boolean> {
	public static enum Operator {
		AND,
		OR
	}
	
	public BooleanOperation(Expression<Boolean> left, Expression<Boolean> right, Operator operator) {
		super(Boolean.class, left, right, operator);
	}
	
	public <V> V accept(ExpressionVisitor<V> visitor) {
		return visitor.visit(this);
	}
}
