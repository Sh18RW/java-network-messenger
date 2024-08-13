package server.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBase {
	private final Properties applicationProperties;
	private final Properties secretProperties;
	private final Logger logger;

	private Connection connection = null;

	public DataBase(Properties application, Properties secret) {
		this.applicationProperties = application;
		this.secretProperties = secret;
		this.logger = LogManager.getLogger(getClass());
	}

	/**
	 * Initializing SQL database connection.
	 * Use application and secret properties.
	 * If something has not set, it causes errors and exceptions.
	 * Has secret values logging in debug mode.
	 * @throws SQLException if sql connection initializing was failed.
	 * @throws IllegalStateException if some properties hasn't set.
	 */
	public void initConnection() throws SQLException {
		if (isConnectionInitialized()) {
			logger.error("Tried to initialize database connection when it has already initialized!");
			return;
		}

		logger.info("Started initializing database connecting.");
		String hostname = applicationProperties.getProperty("database.hostname",
				"localhost");
		String port = applicationProperties.getProperty("database.port",
				"5432");

		String username = secretProperties.getProperty("database.user", "");
		String password = secretProperties.getProperty("database.password", "");

		String databaseName = applicationProperties.getProperty("database.name", "");
		String driver = applicationProperties.getProperty("database.driver", "");

		if (username.isEmpty()) {
			logger.error("database.user secret property is empty!");
			throw new IllegalStateException("database.user is empty");
		}

		if (driver.isEmpty()) {
			logger.error("database.driver property is empty!");
			throw new IllegalStateException("database.driver is empty");
		}

		if (databaseName.isEmpty()) {
			logger.error("database.name property is empty!");
			throw new IllegalStateException("database.name is empty");
		}

		String connectionUrl = String.format("jdbc:%s://%s:%s/%s",
				driver, hostname, port, databaseName);

		/*
		Attention! Next log information saves username and password!
		Be careful, it should be disabled in Release mode!
		TODO: disable debug logging in release mode
		 */
		logger.debug(String.format("Try to open connection \"%s\" %s:%s",
				connectionUrl, username, password));

		connection = DriverManager.getConnection(connectionUrl, username, password);
	}

	/**
	 * Checks {@link DataBase#connection} for null value.
	 * @return true if {@link DataBase#connection} was initialized.
	 */
	public boolean isConnectionInitialized() {
		return connection != null;
	}

	public void closeConnection() {
	}
}
