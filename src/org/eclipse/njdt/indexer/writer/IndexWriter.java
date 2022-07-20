package org.eclipse.njdt.indexer.writer;

public interface IndexWriter {
	IndexWriterDocumentSession beginIndexing(DocumentAddress document);
}
