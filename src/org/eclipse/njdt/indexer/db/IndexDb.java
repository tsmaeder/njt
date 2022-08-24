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
import java.sql.SQLException;

import org.postgresql.ds.PGPoolingDataSource;

@SuppressWarnings("deprecation")
public class IndexDb {
	private PGPoolingDataSource dataSource;

	public IndexDb(String db, String username, String password) {
		dataSource = new PGPoolingDataSource();
		String url = "jdbc:postgresql://"+db;
		dataSource.setUrl(url);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		dataSource.setDataSourceName("indexDbSource");
	}
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection(); 
	}

	public void close() {
		this.dataSource.close();
	}
}
