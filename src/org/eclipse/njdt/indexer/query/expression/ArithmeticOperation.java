package org.eclipse.njdt.indexer.query.expression;

public class ArithmeticOperation extends BinaryOperation<ArithmeticOperation.Operator, Number, Number, Number> {
	public enum Operator {
		Plus,
		Minus,
		Times,
		Div
	}
	
	public ArithmeticOperation(Expression<Number> left, Expression<Number> right, Operator operator) {
		super(Number.class, left, right, operator);
	}
}
