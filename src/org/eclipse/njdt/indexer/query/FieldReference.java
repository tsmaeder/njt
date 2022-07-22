package org.eclipse.njdt.indexer.query;

import org.eclipse.njdt.indexer.writer.FieldReferenceKind;

public interface FieldReference extends Reference<FieldReferenceKind> {
	CharSequence name();
}
