package co.sgsl.core;

import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

/**
 * Monitor the property file for changes and if any, update the property cache
 * 
 * @author prakash.raman
 *
 */
public class PropertyFileMonitor{
	private DefaultFileMonitor fileMonitor;
	private FileObject listenFile;
	
	public PropertyFileMonitor(String filename, FileListener listener) throws Exception {
		FileSystemManager fsManager = VFS.getManager();
		 listenFile = fsManager.resolveFile(filename);

		 fileMonitor = new DefaultFileMonitor(listener);
		 fileMonitor.setRecursive(true);
		 fileMonitor.addFile(listenFile);
	}
	
	public void startMonitoring(){
		fileMonitor.start();
	}
	
	public void stopMonitoring() throws FileSystemException{
		listenFile.close();
		fileMonitor.stop();
	}
}
