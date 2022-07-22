package org.eclipse.njdt.indexer.query;

import java.util.List;

public interface IndexReader {
	<T extends Reference<?>> List<T> queryReferences(ReferenceQuery<T> query);
}
