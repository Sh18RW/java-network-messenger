package ru.sh18rw.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestNetwork {
	public static void testMethod(String message) {
		Logger logger = LogManager.getLogger();
		
		logger.info(message);
	}
}
