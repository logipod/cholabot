package co.sgsl.core;


import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;


public class AppProperties implements FileListener{

	private static AppProperties appProperties = null;
	public static String TEMP_PATH = "";
	private Properties appProp = null;
	private String configFilePath = null;
	private FileInputStream appStream;
	

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
		try {
			refresh();
			PropertyFileMonitor fileMonitor = new PropertyFileMonitor(configFilePath, this);
			fileMonitor.startMonitoring();
		} catch (Exception e) {
			LoggerUtil.getLogger().error("[AppProperties - setConfigFilePath] Exception occured", e);
		}
	}

	
	/**
	 * Returns an existing object of type AppConfig.
	 * If the Object doesnot exists then a new object
	 *  is created and returned.
	 *  
	 * @return AppConfig - returns the Object of this class
	 **/
	public static synchronized AppProperties getInstance(){
		if (appProperties==null){
			appProperties = new AppProperties();
		}
		return appProperties;
	}

	/**
	 * Checks whether the file has been changed and
	 *  loads the properties from the file.
	 * @throws Exception 
	 * 
	 **/
	private void refresh() throws Exception{
			if (appStream!=null)
				appStream.close();

			if (appProp!=null)
				appProp.clear();
			else
				appProp = new Properties();

			appStream = new FileInputStream(configFilePath);
			appProp.load(appStream);// Loads the properties.
	}
	
	/* Returns the value of the property name that
	 *  is passed as a parameter. If there is no
	 *  value available then the value "undefined"
	 *  is returned.
	 *
	 * @param String  - Property Name.
	 * 
	 * @return String - Return the value corresponding to the property name.
	 * */
	public String getProperty(String propertyName){

		String propertyValue=AppConstants.UNDEFINED;
		propertyValue = appProp.getProperty(propertyName,propertyValue);

		return propertyValue;
	}

	public Properties getAppProp() {
		return appProp;
	}


	@Override
	public void fileChanged(FileChangeEvent arg0) throws Exception {
		LoggerUtil.debug("[AppProperties - fileChanged] " + arg0.getFile().getName().getBaseName() + " changed. Updating cache");
		refresh();
	}
	@Override
	public void fileCreated(FileChangeEvent arg0) throws Exception {
	}
	@Override
	public void fileDeleted(FileChangeEvent arg0) throws Exception {
	}
}
