package org.eclipse.njdt.indexer.query;

import org.eclipse.njdt.indexer.writer.MethodReferenceKind;

public interface MethodReference extends Reference<MethodReferenceKind> {
	CharSequence signature();
}
