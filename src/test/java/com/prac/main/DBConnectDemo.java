package com.prac.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class DBConnectDemo {
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://172.29.140.77:3306/mysql", "arvind", "admin");
			ResultSet rs = con.createStatement().executeQuery("select * from user");
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++)
					System.out.print(rs.getString(i) + "\t\t");
				System.out.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
