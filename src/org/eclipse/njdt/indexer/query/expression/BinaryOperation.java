package org.eclipse.njdt.indexer.query.expression;

public class BinaryOperation<O, L, R, T> extends Expression<T> {
	private Expression<L> left;
	private Expression<R> right;
	private O operator;

	public BinaryOperation(Class<T> type, Expression<L> left, Expression<R> right, O operator) {
		super(type);
		this.left= left;
		this.right= right;
		this.operator= operator;
	}
	
	public Expression<L> getLeft() {
		return left;
	}
	
	public Expression<R> getRight() {
		return right;
	}
	
	public O getOperator() {
		return operator;
	}}
