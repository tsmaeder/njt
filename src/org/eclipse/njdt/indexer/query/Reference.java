package org.eclipse.njdt.indexer.query;

import org.eclipse.njdt.indexer.writer.IntValue;
import org.eclipse.njdt.indexer.writer.Range;

public interface Reference<T extends IntValue> {
	T kind();
	CharSequence typeMoniker();
	boolean onDemand();
	Range sourceRange();
}
