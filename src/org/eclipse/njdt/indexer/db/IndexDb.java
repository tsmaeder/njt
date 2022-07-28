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
	}
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection(); 
	}

	public void close() {
		this.dataSource.close();
	}
}
