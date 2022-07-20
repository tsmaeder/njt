package org.eclipse.njdt.indexer.writer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.eclipse.njdt.indexer.MonikerFactory;
import org.eclipse.njdt.indexer.writer.DocumentAddress;
import org.eclipse.njdt.indexer.writer.FieldReferenceKind;
import org.eclipse.njdt.indexer.writer.IndexWriterDocumentSession;
import org.eclipse.njdt.indexer.writer.MethodReferenceKind;
import org.eclipse.njdt.indexer.writer.Range;
import org.eclipse.njdt.indexer.writer.TypeReferenceKind;

public class DBIndexSession implements IndexWriterDocumentSession {
	private Connection connection;
	private DocumentAddress address;
	private MonikerFactory monikerFactoy;
	private DBIndexWriter writer;
	private PreparedStatement insertDeclarationStatement;
	private PreparedStatement insertReferenceStatement;

	public DBIndexSession(DBIndexWriter writer, MonikerFactory monikerFactory, DocumentAddress address, Connection c) throws SQLException {
		this.writer= writer;
		this.monikerFactoy= monikerFactory;
		this.address= address;
		this.connection = c;
		
		this.insertDeclarationStatement = connection
				.prepareStatement("insert into declarations(index_id, document_id, modifiers, type_name, name, signature, source_start, source_length) "
						+ "values(?, ?, ?, ?, ?, ?, ?, ?)");
		
		this.insertReferenceStatement = connection.prepareStatement("insert into refs (index_id, document_id, type_moniker, on_demand, name, signature, source_start, source_end) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?)");
	}

	@Override
	public void addTypeDeclaration(int modifiers, CharSequence qualifiedTypeName, Range sourceRange) {
		try {
			insertDeclarationStatement.clearParameters();
			insertDeclarationStatement.setString(1, address.indexId().toString());
			insertDeclarationStatement.setString(2, address.documentId().toString());
			insertDeclarationStatement.setInt(3, modifiers);
			insertDeclarationStatement.setString(4, qualifiedTypeName.toString());
			insertDeclarationStatement.setNull(5, Types.VARCHAR);
			insertDeclarationStatement.setNull(6, Types.VARCHAR);
			if (sourceRange != null) {
				insertDeclarationStatement.setInt(7, sourceRange.start());
				insertDeclarationStatement.setInt(8, sourceRange.length());
			} else {
				insertDeclarationStatement.setNull(7, Types.INTEGER);
				insertDeclarationStatement.setNull(8, Types.INTEGER);
			}
			insertDeclarationStatement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addMethodDeclaration(int modifiers, CharSequence qualifiedTypeName, CharSequence methodName,
			CharSequence signature, Range sourceRange) {
		try {
			insertDeclarationStatement.clearParameters();
			insertDeclarationStatement.setString(1, address.indexId().toString());
			insertDeclarationStatement.setString(2, address.documentId().toString());
			insertDeclarationStatement.setInt(3, modifiers);
			insertDeclarationStatement.setString(4, qualifiedTypeName.toString());
			insertDeclarationStatement.setString(5, methodName.toString());
			insertDeclarationStatement.setString(6, signature.toString());
			if (sourceRange != null) {
				insertDeclarationStatement.setInt(7, sourceRange.start());
				insertDeclarationStatement.setInt(8, sourceRange.length());
			} else {
				insertDeclarationStatement.setNull(7, Types.INTEGER);
				insertDeclarationStatement.setNull(8, Types.INTEGER);
			}
			insertDeclarationStatement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addFieldDeclaration(int modifiers, CharSequence qualifiedTypeName, CharSequence fieldName,
			CharSequence fieldSignature, Range sourceRange) {
		try {
			insertDeclarationStatement.clearParameters();
			insertDeclarationStatement.setString(1, address.indexId().toString());
			insertDeclarationStatement.setString(2, address.documentId().toString());
			insertDeclarationStatement.setInt(3, modifiers);
			insertDeclarationStatement.setString(4, qualifiedTypeName.toString());
			insertDeclarationStatement.setString(5, fieldName.toString());
			insertDeclarationStatement.setString(6, fieldSignature.toString());
			if (sourceRange != null) {
				insertDeclarationStatement.setInt(7, sourceRange.start());
				insertDeclarationStatement.setInt(8, sourceRange.length());
			} else {
				insertDeclarationStatement.setNull(7, Types.INTEGER);
				insertDeclarationStatement.setNull(8, Types.INTEGER);
			}
			insertDeclarationStatement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	}

	@Override
	public void addTypeReference(TypeReferenceKind kind, CharSequence typeName, boolean isOnDemandImport, Range sourceRange) {
		try {
			
			insertReferenceStatement.clearParameters();
			insertReferenceStatement.setString(1, address.indexId().toString());
			insertReferenceStatement.setString(2, address.documentId().toString());
			insertReferenceStatement.setString(3, monikerFactoy.createTypeMoniker(address, typeName).toString());
			insertReferenceStatement.setBoolean(4, isOnDemandImport);
			insertReferenceStatement.setNull(5, Types.VARCHAR);
			insertReferenceStatement.setNull(6, Types.VARCHAR);
			if (sourceRange != null) {
				insertReferenceStatement.setInt(7, sourceRange.start());
				insertReferenceStatement.setInt(8, sourceRange.length());
			} else {
				insertReferenceStatement.setNull(7, Types.INTEGER);
				insertReferenceStatement.setNull(8, Types.INTEGER);
			}
			insertReferenceStatement.execute();			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	}

	@Override
	public void addMethodReference(MethodReferenceKind kind, CharSequence typeName, boolean isOnDemandImport, CharSequence methodName,
			CharSequence signature, Range sourceRange) {
		try {
			
			insertReferenceStatement.clearParameters();
			insertReferenceStatement.setString(1, address.indexId().toString());
			insertReferenceStatement.setString(2, address.documentId().toString());
			insertReferenceStatement.setString(3, monikerFactoy.createTypeMoniker(address, typeName).toString());
			insertReferenceStatement.setBoolean(4, isOnDemandImport);
			insertReferenceStatement.setString(5, methodName.toString());
			insertReferenceStatement.setString(6, signature.toString());
			if (sourceRange != null) {
				insertReferenceStatement.setInt(7, sourceRange.start());
				insertReferenceStatement.setInt(8, sourceRange.length());
			} else {
				insertReferenceStatement.setNull(7, Types.INTEGER);
				insertReferenceStatement.setNull(8, Types.INTEGER);
			}
			insertReferenceStatement.execute();			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addFieldReference(FieldReferenceKind kind, CharSequence typeName, boolean isOnDemandImport, 
			CharSequence fieldName, Range sourceRange) {
		try {
			
			insertReferenceStatement.clearParameters();
			insertReferenceStatement.setString(1, address.indexId().toString());
			insertReferenceStatement.setString(2, address.documentId().toString());
			insertReferenceStatement.setString(3, monikerFactoy.createTypeMoniker(address, typeName).toString());
			insertReferenceStatement.setBoolean(4, isOnDemandImport);
			insertReferenceStatement.setString(5, fieldName.toString());
			insertReferenceStatement.setNull(6, Types.VARCHAR);
			if (sourceRange != null) {
				insertReferenceStatement.setInt(7, sourceRange.start());
				insertReferenceStatement.setInt(8, sourceRange.length());
			} else {
				insertReferenceStatement.setNull(7, Types.INTEGER);
				insertReferenceStatement.setNull(8, Types.INTEGER);
			}
			insertReferenceStatement.execute();			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void done() {
		this.writer.done(address);
	}
}
