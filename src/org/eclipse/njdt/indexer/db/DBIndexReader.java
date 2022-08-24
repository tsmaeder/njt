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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.njdt.indexer.Range;
import org.eclipse.njdt.indexer.query.IndexReader;
import org.eclipse.njdt.indexer.query.Reference;
import org.eclipse.njdt.indexer.query.TypeDeclaration;
import org.eclipse.njdt.indexer.query.expression.Expression;
import org.eclipse.njdt.indexer.writer.DocumentAddress;

public class DBIndexReader implements IndexReader {
	private IndexDb db;
	private Connection connection;

	public DBIndexReader(IndexDb db) {
		this.db= db;
	}
	
	public void connect() throws SQLException {
		this.connection= db.getConnection();
	}
	
	public void close() throws SQLException {
		this.connection.close();
	}
	
	@Override
	public <T extends Reference<?>> List<T> queryDeclarations(Class<T> what, Expression<Boolean> where) {
		try {
			List<T> result= new ArrayList<>();
			PreparedStatement statement= new DBQuery(what, where).createStatement(connection);
			ResultSet cursor = statement.executeQuery();
			while (cursor.next()) {
				createDeclaration(what, cursor);
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private <T> void createDeclaration(Class<T> what, ResultSet cursor) {
	}

	@Override
	public <T extends Reference<?>> List<T> queryReferences(Class<T> what, Expression<Boolean> where) {
		return null;
	}
}

record TypeDecl(DocumentAddress address, int modifiers, CharSequence qualifiedTypeName, Range sourceRange) implements TypeDeclaration {

};
