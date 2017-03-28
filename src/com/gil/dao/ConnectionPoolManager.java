package com.gil.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gil.enums.ErrorType;
import com.gil.exceptions.ApplicationException;

public class ConnectionPoolManager {

	private static ConnectionPoolManager instance = null;
	private static Object mutex = new Object();

	final int MAX_POOL_SIZE = 5;
	ArrayList<Connection> connectionPool = new ArrayList<Connection>();
	ArrayList<Connection> connectionsInUse = new ArrayList<Connection>();

	// Database connection parameters
	String databaseUrl = "jdbc:mysql://localhost:3306/coupons_project?useSSL=false";
	String userName = "root";
	String password = "12345";

	public static ConnectionPoolManager getInstance() throws ApplicationException {

		synchronized (mutex) {
			if (instance == null) {
				instance = new ConnectionPoolManager();
			}
		}
		return instance;
	}

	// private constructor. Creates the pool on initialization
	private ConnectionPoolManager() throws ApplicationException {
		while (connectionPool.size() < MAX_POOL_SIZE) {
			connectionPool.add(createNewConnectionForPool());
		}
	}

	// Creating a connection
	private Connection createNewConnectionForPool() throws ApplicationException {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(databaseUrl, userName, password);
		} catch (ClassNotFoundException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, e.getMessage());
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, e.getMessage());
		}
		return connection;
	}

	public Connection getConnection() throws ApplicationException {
		Connection connection = null;

		synchronized (this) {
			if (connectionPool.size() == 0) {
				try {
					// wait for a connection to be available or break after 30
					// seconds
					wait(30 * 1000);
				} catch (InterruptedException e) {
					throw new ApplicationException(ErrorType.GENERAL_ERROR, e, e.getMessage());
				}
			}

			connection = (Connection) connectionPool.get(0);
			connectionsInUse.add(connection);
			connectionPool.remove(connection);

		}

		return connection;
	}

	public void returnConnection(Connection connection) {
		synchronized (this) {
			connectionPool.add(connection);
			connectionsInUse.remove(connection);
			//wake up all waiting connections
			notifyAll();
		}

	}

	// use this method on system shutdown
	public void closeAllConnections() throws ApplicationException {
		synchronized (this) {
			try {
				for (Connection connection : connectionPool) {
					connection.close();
				}
				for (Connection connection : connectionsInUse) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.GENERAL_ERROR, e, e.getMessage());
			}
		}
	}
}
