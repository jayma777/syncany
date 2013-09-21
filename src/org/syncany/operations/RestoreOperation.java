package org.syncany.operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.syncany.config.Config;
import org.syncany.database.Database;
import org.syncany.database.DatabaseVersion;
import org.syncany.database.FileVersion;
import org.syncany.database.FileVersion.FileStatus;
import org.syncany.database.PartialFileHistory;
import org.syncany.operations.LoadDatabaseOperation.LoadDatabaseOperationResult;
import org.syncany.operations.Operation.OperationOptions;
import org.syncany.util.FileLister;
import org.syncany.util.FileLister.FileListerAdapter;
import org.syncany.util.FileUtil;

public class RestoreOperation extends Operation {
	private static final Logger logger = Logger.getLogger(RestoreOperation.class.getSimpleName());	
	private RestoreOperationOptions options;
	
	public RestoreOperation(Config config) {
		this(config, null);
	}	
	
	public RestoreOperation(Config config, RestoreOperationOptions options) {
		super(config);		
		this.options = options;
	}	
	
	public OperationResult execute() throws Exception {
		logger.log(Level.INFO, "");
		logger.log(Level.INFO, "Running 'Restore' at client "+config.getMachineName()+" ...");
		logger.log(Level.INFO, "--------------------------------------------");
		
		Database database = ((LoadDatabaseOperationResult) new LoadDatabaseOperation(config).execute()).getDatabase();		
		DatabaseVersion currentDatabaseVersion = database.getLastDatabaseVersion();
		
		if (currentDatabaseVersion == null) {
			throw new Exception("No database versions yet locally. Nothing to revert.");
		}

		Date restoreDate = options.getRestoreTime();
		List<DatabaseVersion> restoreDatabaseVersions = findDatabaseVersionBeforeRestoreTime(database, restoreDate);
		
		List<String> restoreFilePaths = options.getRestoreFilePaths();
		List<FileVersion> restoreFileVersions = findRestoreFileVersions(database, restoreDatabaseVersions, restoreFilePaths);
		
		return new RestoreOperationResult();
	}	
	
	
	
	private List<FileVersion> findRestoreFileVersions(Database database, List<DatabaseVersion> restoreDatabaseVersions, List<String> restoreFilePaths) {
		Set<Long> usedRestoreFileHistoryIds = new HashSet<Long>();
		List<String> leftOverRestoreFilePaths = new ArrayList<String>(restoreFilePaths);

		List<FileVersion> restoreFileVersions = new ArrayList<FileVersion>();
		
		for (int i=restoreDatabaseVersions.size()-1; i>=0; i--) {
			DatabaseVersion currentDatabaseVersion = restoreDatabaseVersions.get(i);
			List<String> removeRestoreFilePaths = new ArrayList<String>();
			
			for (String restoreFilePath : leftOverRestoreFilePaths) {
				//currentDatabaseVersion.getF
				throw new RuntimeException("Not yet implemented.");
			}
		}
		
		return restoreFileVersions;
	}

	private List<DatabaseVersion> findDatabaseVersionBeforeRestoreTime(Database database, Date restoreDate) {
		List<DatabaseVersion> earlierOrEqualDatabaseVersions = new ArrayList<DatabaseVersion>();
		
		for (DatabaseVersion compareDatabaseVersion : database.getDatabaseVersions()) {
			if (compareDatabaseVersion.getTimestamp().equals(restoreDate) || compareDatabaseVersion.getTimestamp().before(restoreDate)) {
				earlierOrEqualDatabaseVersions.add(compareDatabaseVersion);
			}
		}
		
		return earlierOrEqualDatabaseVersions;
	}



	public static class RestoreOperationOptions implements OperationOptions {
		private Date restoreTime;
		private List<String> restoreFilePaths;
		private boolean force;
		
		public Date getRestoreTime() {
			return restoreTime;
		}
		
		public void setRestoreTime(Date restoreTime) {
			this.restoreTime = restoreTime;
		}
		
		public List<String> getRestoreFilePaths() {
			return restoreFilePaths;
		}
		
		public void setRestoreFilePaths(List<String> restoreFiles) {
			this.restoreFilePaths = restoreFiles;
		}

		public boolean isForce() {
			return force;
		}

		public void setForce(boolean force) {
			this.force = force;
		}					
	}
	
	public class RestoreOperationResult implements OperationResult {
		// Fressen
	}
}