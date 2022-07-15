package org.eclipse.njdt.indexer;

import org.eclipse.njdt.indexer.writer.IndexWriter;

public interface IndexedLocation {
	CharSequence lookupTypeId(CharSequence typeName);
	String getIndexUri();
	void index(IndexWriter indexWriter, MonikerFactory monikerFactory);
}
