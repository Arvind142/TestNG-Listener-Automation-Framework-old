package com.experimental;

import java.io.File;
import java.io.FileInputStream;
import java.net.ConnectException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTP_Example {
	public static void main(String[] args) {
		FTPClient client = new FTPClient();
		String server = "172.31.89.36";
		int port = 21;
		String user = "ftpuser";
		String pass = "admin";
		try {
			// connecting to ftp server
			client.connect(server, port);
			System.out.println(client.getReplyString());

			// login
			boolean connectStatus = client.login(user, pass);
			System.out.println("Status: " + connectStatus);
			if (!connectStatus) {
				throw new Exception("Login failed");
			}

			// fileupload
			client.enterLocalPassiveMode();
			client.setFileType(FTP.BINARY_FILE_TYPE);

			String uploadFolder = "ReportDirectory/Reports";
			File img = new File("tutorialpoint.png");
			client.changeWorkingDirectory(uploadFolder);
			for (String s : client.listNames())
				System.out.println(s);
			String localFile = "ABC";
			String remoteFile = "XYZ/";
			client.mkd(remoteFile);
			client.changeWorkingDirectory(remoteFile);
			boolean upload = client.storeFile(img.getName(), new FileInputStream(img));

			System.out.println("uplaod status:" + upload);

			client.logout();
			client.disconnect();
		} catch (ConnectException e) {
			System.out.println("invalid Server address");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
