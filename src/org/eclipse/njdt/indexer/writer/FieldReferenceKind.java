package org.eclipse.njdt.indexer.writer;

public enum FieldReferenceKind implements IntValue {
	None(20),
	Read(21),
	Write(22),
	Both(23);

	private int value;

	FieldReferenceKind(int i) {
		this.value= i;
	}

	@Override
	public int getValue() {
		return value;
	}
	
	static FieldReferenceKind forValue(int value) {
		return values()[value-None.getValue()];
	}
}
