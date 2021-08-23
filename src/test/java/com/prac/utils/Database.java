package com.prac.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Database deals with all db related functions it holds many re-usable method
 * which could ease work
 * 
 * @author arvin
 *
 */
public class Database {

	/***
	 * getMySQLDBConnection method can be used to get connection established to
	 * mySQL DB
	 * 
	 * @param host     dbhost
	 * @param username dbusername
	 * @param password dbpassword
	 * @return return connection of java.sql.connection type when connection is
	 *         successful or else null
	 */
	public Connection getMySqlDBConnection(String host, String username, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://" + host, username, password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
