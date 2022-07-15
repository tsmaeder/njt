package org.eclipse.njdt.indexer;

import org.eclipse.njdt.indexer.writer.DocumentAddress;

public interface MonikerFactory {
	CharSequence createTypeMoniker(DocumentAddress referenceFrom, CharSequence typeName);
}
