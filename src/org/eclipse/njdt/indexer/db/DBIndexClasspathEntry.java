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
import java.util.List;

import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.IModule;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.njdt.indexer.ClasspathEntry;
import org.eclipse.njdt.indexer.ModuleMatchingStrategy;

/**
 * A class path entry the uses the index to build types to compile against. If we get this to work,
 * we could just compile against a pre-built index. I suspect this would be faster than parsing class
 * files repeatedly.
 * @author Thomas Mäder
 *
 */
public class DBIndexClasspathEntry implements ClasspathEntry {
	private IndexDb indexDb;
	private Connection connection;
	private PreparedStatement findTypeInModule;
	private PreparedStatement findTypeInAnyNamedModule;
	private PreparedStatement findTypeInUnnamedModule;
	private PreparedStatement findTypeInAnyModule;

	public DBIndexClasspathEntry(IndexDb db) {
		this.indexDb = db;
	}

	public void connect() throws SQLException {
		this.connection = indexDb.getConnection();
		this.findTypeInModule = connection
				.prepareStatement("select * from declarations where " + "module_name = ? and type_name = ?");
		this.findTypeInAnyNamedModule = connection
				.prepareStatement("select * from declarations where " + "module_name is not null and type_name = ?");
		this.findTypeInUnnamedModule = connection
				.prepareStatement("select * from declarations where " + "module_name is null and type_name = ?");
		this.findTypeInAnyModule = connection.prepareStatement("select * from declarations where " + "type_name = ?");
	}

	public void close() throws SQLException {
		this.connection.close();
	}

	@Override
	public NameEnvironmentAnswer findType(char[] moduleName, String typeName) {
		try {
			ModuleMatchingStrategy matchingStrategy = ModuleMatchingStrategy.fromModuleName(moduleName);
			PreparedStatement statement;
			switch (matchingStrategy) {
			case Named: {
				statement = findTypeInAnyNamedModule;
				statement.clearParameters();
				statement.setString(0, new String(moduleName));
				statement.setString(1, typeName);
				break;
			}
			case AnyNamed: {
				statement = findTypeInAnyNamedModule;
				statement.clearParameters();
				statement.setString(0, typeName);
				break;
			}
			case Unnamed: {
				statement = findTypeInUnnamedModule;
				statement.clearParameters();
				statement.setString(0, typeName);
				break;
			}
			default: {
				statement = findTypeInAnyModule;
				statement.clearParameters();
				statement.setString(0, typeName);
				break;
			}
			}
			ResultSet result = statement.executeQuery();

			IBinaryType b = null;

			// build binary type from the method and field declarations in the db.
			// probably needs some information from the references (supertypes, etc.).
			// also, need to remember some more info from the classfile like "source name",
			// etc.

			return new NameEnvironmentAnswer(b, null);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String[] getModulesDeclaringPackage(String binaryPackageName, ModuleMatchingStrategy fromModuleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCompilationUnit(String concatWith, char[] moduleName, boolean checkCUs) {
		// we report "binary types" only
		return false;
	}

	@Override
	public IModule getModule(char[] moduleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listPackages(char[] moduleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAutomaticModules() {
		// TODO Auto-generated method stub
		return null;
	}

}
