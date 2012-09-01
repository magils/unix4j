package org.unix4j.command;

import java.io.File;
import java.util.Map;
import java.util.Properties;

public class DefaultExecutionContext implements ExecutionContext {
	private String user; 
	private File userHome; 
	private File tempDirectory; 
	private File currentDirectory; 
	public DefaultExecutionContext() {
		this.currentDirectory = null;//default
	}
	public DefaultExecutionContext(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}
	public void setCurrentDirectory(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}
	@Override
	public File getCurrentDirectory() {
		if (currentDirectory == null) {
			currentDirectory = new File(System.getProperty("user.dir"));
		}
		return currentDirectory;
	}
	@Override
	public String getUser() {
		if (user == null) {
			user = System.getProperty("user.name");
		}
		return user;
	}
	@Override
	public File getUserHome() {
		if (userHome == null) {
			userHome = new File(System.getProperty("user.home"));
		}
		return userHome;
	}
	@Override
	public File getTempDirectory() {
		if (tempDirectory == null) {
			tempDirectory = new File(System.getProperty("java.io.tmpdir"));
		}
		return tempDirectory;
	}
	@Override
	public Map<String, String> getEnv() {
		return System.getenv();
	}
	@Override
	public Properties getSys() {
		return System.getProperties();
	}
}