package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App {
    private static final String APPLICATION_PROPERTIES_FILENAME = "application.properties";

    private final Logger logger;

    public static void main(String[] args) {
        new App().start();
    }

    public App() {
        logger = LogManager.getLogger(getClass());
    }

    public void start() {
        Properties properties = loadProperties();

        int port = Integer.parseInt(properties.getProperty("server.port", "5678"));

        Server server = new Server(port);

        try {
            server.initSocket();
			server.closeSocket();
        } catch (IOException e) {
            logger.fatal(String.format(
					"Server socket was not started. It's fatal error, can't continue anymore. Exception: %s",
					e.getMessage()));
        }
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES_FILENAME)) {
            properties.load(input);
        } catch (IOException e) {
            logger.error(String.format(
                    "Can't read %s property file. It can cause some errors, but application still continue...",
					APPLICATION_PROPERTIES_FILENAME));
        }

        return properties;
    }
}
