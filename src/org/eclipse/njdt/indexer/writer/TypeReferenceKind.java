package org.eclipse.njdt.indexer.writer;

public enum TypeReferenceKind {
	Imports,
	Extends,
	Implements,
	Permitted,
	Annotation,
	FieldType,
	VariableType,
	MethodReturn,
	MethodParameter,
	ThrownException,
	CaughtException,
	ParameterBounds,
	WildcardBounds,
	TypeArgument,
	Cast,
	InstanceCreation,
	InstanceOf,
	Unknown
}
