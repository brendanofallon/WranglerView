package wranglerView.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import wranglerView.server.WranglerProperties;

/**
 * Since there is no entry point on the server side we use this class to make sure
 * that the necessary log handlers are created (just once) while 
 * @author brendanofallon
 *
 */
public class WLogger {

	public static final String name = "wrangler.global"; 
	private static Logger logger = null;
	
	/**
	 * Return the global logger, initialing if necessary
	 * @return
	 */
	public static Logger get() {
		if (logger == null) {
			initialize();
		}
		
		return logger;
	}
	
	/**
	 * Emit a warning message using the default logger
	 * @param message
	 */
	public static void warn(String message) {
		if (logger == null) {
			initialize();
		}
		
		logger.warning(message);
	}
	
	/**
	 * Emit an info-level message using the default logger
	 * @param message
	 */
	public static void info(String message) {
		if (logger == null) {
			initialize();
		}
		
		logger.info(message);
	}
	
	/**
	 * Emit a severe-level message using the default logger
	 * @param message
	 */
	public static void severe(String message) {
		if (logger == null) {
			initialize();
		}
		
		logger.severe(message);
	}

	private static void initialize() {
		logger = Logger.getLogger(name);
		
		String logFilePath = WranglerProperties.getWranglerRoot().getAbsolutePath() + "/wrangler.log";
		try {
			FileHandler handler = new FileHandler(logFilePath, 10000, 1, true);
			handler.setFormatter( new SimpleFormatter() );
			logger.addHandler(handler);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
