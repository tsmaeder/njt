package org.eclipse.njdt.indexer.writer;

public enum MethodReferenceKind implements IntValue {
	ThisReference(30),
	ImplicitThisReference(31),
	SuperReference(32),
	QualifiedReference(33),
	MethodLiteral(34);
	
	public static final int MIN= 30;
	public static final int MAX= 34;

	private int value;

	MethodReferenceKind(int i) {
		this.value= i;
	}

	@Override
	public int getValue() {
		return value;
	}

	static MethodReferenceKind forValue(int value) {
		return values()[value-ThisReference.getValue()];
	}
}
