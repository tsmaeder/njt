package org.eclipse.njdt.indexer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.njdt.indexer.DefaultMonikerFactory;
import org.eclipse.njdt.indexer.IndexedLocation;
import org.eclipse.njdt.indexer.writer.DocumentAddress;
import org.eclipse.njdt.indexer.writer.IndexWriter;
import org.eclipse.njdt.indexer.writer.IndexWriterDocumentSession;

public class DBIndexWriter implements IndexWriter {
	private Connection connection;
	private List<IndexedLocation> indexedLocations;
	private PreparedStatement deleteDeclarationsStatement;
	private PreparedStatement deleteReferencesStatement;
	private IndexDb indexDb;

	public DBIndexWriter(List<IndexedLocation> locations, IndexDb db) {
		this.indexedLocations= locations;
		this.indexDb= db;
	}
	
	public void connect() throws SQLException {
		this.connection = indexDb.getConnection();
		connection.setAutoCommit(false);
		this.deleteDeclarationsStatement= connection.prepareStatement("delete from declarations where index_id= ? and document_id=?");
		this.deleteReferencesStatement= connection.prepareStatement("delete from refs where index_id= ? and document_id=?");
	}
	
	public void close() throws SQLException {
		this.connection.close();
	}
	
	@Override
	public IndexWriterDocumentSession beginIndexing(DocumentAddress document) {
		System.out.println("Indexing: "+document);
		try {
			deleteDeclarationsStatement.clearParameters();
			deleteDeclarationsStatement.setString(1, document.indexId().toString());
			deleteDeclarationsStatement.setString(2, document.documentId().toString());
			deleteDeclarationsStatement.execute();
			deleteReferencesStatement.clearParameters();
			deleteReferencesStatement.setString(1, document.indexId().toString());
			deleteReferencesStatement.setString(2, document.documentId().toString());
			deleteReferencesStatement.execute();
			return new DBIndexSession(this, new DefaultMonikerFactory(indexedLocations), document, connection);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void done(DocumentAddress address) {
		try {
			this.connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
