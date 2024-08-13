package server;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
	private final int port;
	private final Logger logger;
	
	private ServerSocket serverSocket = null;
	
	public Server(int port) {
		this.port = port;
		
		this.logger = LogManager.getLogger(this.getClass());
	}

	/**
	 * Initialize ServerSocket on {@link Server#port} port.
	 * If it fails, server can't work more.
	 * You should stop initializing and shutdown already initialized services.
	 * If initializing fails, it generates exception.
	 * You needn't check something else.
	 * Initializes {@link Server#serverSocket}.
	 * @throws IOException if ServerSocket opening failed, this exception throws.
	 */
	public void initSocket() throws IOException {
		if (isServerStarted()) {
			logger.error("Tried to initialize server socket when it has already done.");
			return;
		}
		
		logger.debug(String.format("Initializing server socket on %d port.", port));
		serverSocket = new ServerSocket(port);
		logger.info(String.format("Server socket was successfully initialized on %d port.", port));
	}

	/**
	 * Closing ServerSocket.
	 * Sets {@link Server#serverSocket} to null.
	 * @throws IOException if ServerSocket closing fails, it generates exception.
	 */
	public void closeSocket() throws IOException {
		if (!isServerStarted()) {
			logger.error("Tried to close server socket when it hasn't been started.");
			return;
		}

		logger.debug("Closing server socket.");
		serverSocket.close();
		logger.info("Server socket was successfully closed.");

		serverSocket = null;
	}

	/**
	 * Checks {@link Server#serverSocket} for null value.
	 * @return true if {@link Server#serverSocket} was initialized.
	 */
	public boolean isServerStarted() {
		return serverSocket != null;
	}
}
