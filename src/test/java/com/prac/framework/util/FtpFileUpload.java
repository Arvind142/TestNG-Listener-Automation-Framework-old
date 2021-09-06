package com.prac.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpFileUpload {
	private FTPClient client = new FTPClient();
	private String server = null;
	private int port = 0;
	private String user = null;
	private String pass = null;

	public FtpFileUpload(Properties property) {
		super();
		this.server = property.getProperty("host");
		this.port = Integer.parseInt(property.getProperty("port"));
		this.user = property.getProperty("user");
		this.pass = property.getProperty("pass");
	}

	public boolean connect() {
		try {
			client.connect(server, port);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean login() {
		try {
			boolean connectStatus = client.login(user, pass);
			if (!connectStatus) {
				throw new Exception("Login failed");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean createReportDirectory() {
		try {
			// setting client mode
			client.enterLocalPassiveMode();
			client.setFileType(FTP.BINARY_FILE_TYPE);
			// creating and changing directory

			// change
			client.changeWorkingDirectory("ReportDirectory/Reports");
			int counter = 0;
			for (FTPFile s : client.listDirectories()) {
				s.getName();
				counter++;
			}

			// create
			counter++;

			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
			String folderName = String.valueOf(counter) + "\t- " + (sdf.format(Calendar.getInstance().getTime()));
			client.mkd(folderName);
			client.changeWorkingDirectory(folderName);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean fileUpload(File reportingFolder) {
		try {
			File htmlFolder = reportingFolder;
			for (File file : htmlFolder.listFiles()) {
				if (!client.storeFile(file.getName(), new FileInputStream(file))) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void close() {
		try {
			client.logout();
			client.disconnect();
		} catch (Exception e) {

		}
	}
}
