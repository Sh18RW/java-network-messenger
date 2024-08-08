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
	
	public void initSocket() throws IOException {
		if (isServerStarted()) {
			logger.error("Tried to initialize server socket when it has already done.");
			return;
		}
		
		logger.debug(String.format("Initializing server socket on %d port.", port));
		serverSocket = new ServerSocket(port);
		logger.info(String.format("Server socket was successfully initialized on %d port.", port));
	}

	public void closeSocket() throws IOException {
		if (!isServerStarted()) {
			logger.error("Tried to close server socket when it hasn't been started.");
			return;
		}

		logger.debug("Closing server socket.");
		serverSocket.close();
		logger.info("Server socket was successfully closed.");
	}
	
	public boolean isServerStarted() {
		return serverSocket != null;
	}
}
