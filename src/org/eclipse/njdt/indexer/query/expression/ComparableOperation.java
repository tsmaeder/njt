package org.eclipse.njdt.indexer.query.expression;

public class ComparableOperation<T extends Comparable<T>> extends BinaryOperation<ComparableOperation.Operator, T, T, Boolean> {
	public enum Operator {
		Less,
		Equal,
		Greater
	}
	
	public ComparableOperation(Expression<T> left, Expression<T> right, Operator operator) {
		super(Boolean.class, left, right, operator);
	}

}
