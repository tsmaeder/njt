package org.eclipse.njdt.indexer.query.expression;

public class ExpressionVisitor<T> {
	public T visit(ArithmeticOperation e) {
		return null;
	}
	
	public T visit(BooleanOperation e) {
		return null;
	}
	
	public T visit(ComparableOperation<?> e) {
		return null;
	}
	
	public T visit(StringPredicate e) {
		return null;
	}
	
	public T visit(Literal<?> e) {
		return null;
	}
	
	public T visit(Parameter<?> e) {
		return null;
	}
	
	public T visit(Property<?> e) {
		return null;
	}
}
