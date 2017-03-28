package com.gil.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.gil.dao.ConnectionPoolManager;
import com.gil.exceptions.ApplicationException;

public class JdbcUtils {

	public static void closeResources(Connection connection, PreparedStatement preparedStatement)
			throws ApplicationException {
		if (connection != null) {
			ConnectionPoolManager.getInstance().returnConnection(connection);
		}

		try {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
			// Write to log that we have a resource leak
			e.printStackTrace();
		}
	}

	public static void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet)
			throws ApplicationException {
		closeResources(connection, preparedStatement);
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException e) {
			// Write to log that we have a resource leak
			e.printStackTrace();
		}

	}

	public static void closeResources(Connection connection, PreparedStatement preparedStatement1,
			PreparedStatement preparedStatement2) throws ApplicationException {
		closeResources(connection, preparedStatement1);
		try {
			if (preparedStatement2 != null) {
				preparedStatement2.close();
			}
		} catch (SQLException e) {
			// Write to log that we have a resource leak
			e.printStackTrace();
		}
	}

	public static void closeResources(Connection connection, PreparedStatement preparedStatement1,
			PreparedStatement preparedStatement2, PreparedStatement preparedStatement3) throws ApplicationException {
		closeResources(connection, preparedStatement1, preparedStatement2);
		try {
			if (preparedStatement3 != null) {
				preparedStatement3.close();
			}
		} catch (SQLException e) {
			// Write to log that we have a resource leak
			e.printStackTrace();
		}
	}

	public static void closeResources(PreparedStatement preparedStatement) {
		try {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
			// Write to log that we have a resource leak
			e.printStackTrace();
		}
	}

}
