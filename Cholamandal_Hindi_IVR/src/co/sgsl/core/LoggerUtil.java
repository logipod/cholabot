package co.sgsl.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoggerUtil {
	
	private static Logger logger = LogManager.getLogger(LoggerUtil.class.getName());
	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		LoggerUtil.logger = logger;
	}

	public static void debug(String data)
	{
		logger.debug(data);
	}
	
	public static void info(String data)
	{
		logger.info(data);
	}
	
	public static void warn(String data)
	{
		logger.warn(data);
	}
	
	public static void error(String data)
	{
		logger.error(data);
	}
	
	public static void error(String data, Throwable t)
	{
		logger.error(data,t);
	}
	
	public static void fatal(String data)
	{
		logger.fatal(data);
	}
}
