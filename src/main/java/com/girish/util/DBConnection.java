package com.girish.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
	

	private static final Properties props=new Properties();
	
	static {
		
		try (InputStream in=DBConnection.class.getClassLoader().getResourceAsStream("db.properties")){  //finds file on classpath and opens it as streams
			
			if(in == null) {
				throw new IllegalStateException("db.properties not found in resources");     
			}
			props.load(in);            //reads key value pairs of properties object stream
		} catch (IOException e) {
			throw new IllegalStateException("Could not read db.properties", e);
		}
	}



private DBConnection() {}

public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
            props.getProperty("db.url"),
            props.getProperty("db.user"),
            props.getProperty("db.password"));
}}
