package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class App {
    private static final String APPLICATION_PROPERTIES_FILENAME = "application.properties";

    private final Logger logger;

    private Server server = null;
    private DataBase dataBase = null;

    public static void main(String[] args) {
        App app = new App();

        app.start();
        app.shutdown();
    }

    public App() {
        logger = LogManager.getLogger(getClass());
    }

    public void start() {
        Properties applicationProperties = loadProperties(APPLICATION_PROPERTIES_FILENAME);
        Properties secretProperties = loadProperties(applicationProperties.getProperty("server.secret"));

        int port = Integer.parseInt(applicationProperties.getProperty("server.port", "5678"));

        server = new Server(port);

        try {
            server.initSocket();
        } catch (IOException e) {
            logger.fatal(String.format(
					"Server socket was not started. It's fatal error, can't continue anymore. Exception: %s",
					e.getMessage()));
            return;
        }

        dataBase = new DataBase(applicationProperties, secretProperties);

        try {
            dataBase.initConnection();
        } catch (IllegalStateException | SQLException e) {
            logger.fatal(String.format("Database setup has failed with exception: %s",
                    e.getMessage()));
            return;
        }

        logger.info("App has successfully setup.");
    }

    public void shutdown() {
        logger.info("Shutdown App.");
        if (server != null) {
            logger.debug("Try to shutdown server socket.");
            try {
                server.closeSocket();
                logger.info("Server was successfully shutdown.");
            } catch (IOException e) {
                logger.error(String.format("While server shutdown has caused exception: %s",
                        e.getMessage()));
            }
        }

        if (dataBase != null) {
            logger.debug("Try to shutdown database.");
            dataBase.closeConnection();
            logger.info("Database has successfully shutdown.");
        }
    }

    private Properties loadProperties(String filename) {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            properties.load(input);
        } catch (IOException e) {
            logger.error(String.format(
                    "Can't read %s property file. It can cause some errors, but application still continue...",
                    filename));
        }

        return properties;
    }
}
