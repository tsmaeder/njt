package org.eclipse.njdt.indexer.writer;

public enum TypeReferenceKind implements IntValue {
	
	Imports(1),
	Extends(2),
	Implements(3),
	Permitted(4),
	Annotation(5),
	FieldType(6),
	VariableType(7),
	MethodReturn(8),
	MethodParameter(9),
	ThrownException(10),
	CaughtException(10),
	ParameterBounds(11),
	WildcardBounds(13),
	TypeArgument(14),
	Cast(15),
	InstanceCreation(16),
	InstanceOf(17),
	Unknown(18);
	
	public static final int MIN= 1;
	public static final int MAX= 18;

	private int value;
	
	private TypeReferenceKind(int value) {
		this.value= value;
	}
	
	public int getValue() {
		return value;
	}
	
	static TypeReferenceKind forValue(int value) {
		return values()[value-Imports.getValue()];
	}
}
