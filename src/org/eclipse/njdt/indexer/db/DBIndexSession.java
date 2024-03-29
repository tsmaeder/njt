/*******************************************************************************
 * Copyright (c) 2022 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.njdt.indexer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.eclipse.njdt.indexer.FieldReferenceKind;
import org.eclipse.njdt.indexer.MethodReferenceKind;
import org.eclipse.njdt.indexer.MonikerFactory;
import org.eclipse.njdt.indexer.Range;
import org.eclipse.njdt.indexer.TypeReferenceKind;
import org.eclipse.njdt.indexer.writer.DocumentAddress;
import org.eclipse.njdt.indexer.writer.IndexWriterDocumentSession;

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
				.prepareStatement("insert into declarations(index_id, document_id, modifiers, type_name, name, signature, source_start, source_length, module_name) "
						+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		this.insertReferenceStatement = connection.prepareStatement("insert into refs (index_id, document_id, reference_kind, type_moniker, on_demand, name, signature, source_start, source_end) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
	}

	@Override
	public void addTypeDeclaration(int modifiers, CharSequence module, CharSequence qualifiedTypeName, Range sourceRange) {
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
			insertDeclarationStatement.setString(9, module.toString());
			insertDeclarationStatement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addMethodDeclaration(int modifiers, CharSequence module, CharSequence qualifiedTypeName, CharSequence methodName,
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
			insertDeclarationStatement.setString(9, module.toString());
			insertDeclarationStatement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addFieldDeclaration(int modifiers, CharSequence module, CharSequence qualifiedTypeName, CharSequence fieldName,
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
			insertDeclarationStatement.setString(9, module.toString());
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
			insertReferenceStatement.setInt(3, kind.getValue());
			insertReferenceStatement.setString(4, monikerFactoy.createTypeMoniker(address, typeName).toString());
			insertReferenceStatement.setBoolean(5, isOnDemandImport);
			insertReferenceStatement.setNull(6, Types.VARCHAR);
			insertReferenceStatement.setNull(7, Types.VARCHAR);
			if (sourceRange != null) {
				insertReferenceStatement.setInt(8, sourceRange.start());
				insertReferenceStatement.setInt(9, sourceRange.length());
			} else {
				insertReferenceStatement.setNull(8, Types.INTEGER);
				insertReferenceStatement.setNull(9, Types.INTEGER);
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
			insertReferenceStatement.setInt(3, kind.getValue());
			insertReferenceStatement.setString(4, monikerFactoy.createTypeMoniker(address, typeName).toString());
			insertReferenceStatement.setBoolean(5, isOnDemandImport);
			insertReferenceStatement.setString(6, methodName.toString());
			insertReferenceStatement.setString(7, signature.toString());
			if (sourceRange != null) {
				insertReferenceStatement.setInt(8, sourceRange.start());
				insertReferenceStatement.setInt(9, sourceRange.length());
			} else {
				insertReferenceStatement.setNull(8, Types.INTEGER);
				insertReferenceStatement.setNull(9, Types.INTEGER);
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
			insertReferenceStatement.setInt(3, kind.getValue());
			insertReferenceStatement.setString(4, monikerFactoy.createTypeMoniker(address, typeName).toString());
			insertReferenceStatement.setBoolean(5, isOnDemandImport);
			insertReferenceStatement.setString(6, fieldName.toString());
			insertReferenceStatement.setNull(7, Types.VARCHAR);
			if (sourceRange != null) {
				insertReferenceStatement.setInt(8, sourceRange.start());
				insertReferenceStatement.setInt(9, sourceRange.length());
			} else {
				insertReferenceStatement.setNull(8, Types.INTEGER);
				insertReferenceStatement.setNull(9, Types.INTEGER);
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
