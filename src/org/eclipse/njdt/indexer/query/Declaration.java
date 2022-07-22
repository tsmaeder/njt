package org.eclipse.njdt.indexer.query;

import org.eclipse.njdt.indexer.writer.DocumentAddress;
import org.eclipse.njdt.indexer.writer.Range;

public interface Declaration {
	DocumentAddress address();
	int modifiers();
	CharSequence qualifiedTypeName();
	Range sourceRange();
}
