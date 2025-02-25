package com.xulio.gal;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

public class DownloadFTP {
    public static void showServerReplay (FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();

        if (replies != null && replies.length > 0) {
            for (String aReplay : replies) {
                System.out.println("SERVER: " + aReplay);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String server = "ftp.scene.org";
        int port = 21;
        String user = "anonymous";
        String directory = "/pub";
        Scanner sc = new Scanner(System.in);

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            showServerReplay(ftpClient);

            int replyCode = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return;
            }

            ftpClient.enterLocalPassiveMode();

            boolean success = ftpClient.login(user, "");

            showServerReplay(ftpClient);

            if (!success) {
                System.out.println("Could not login to the server");
                return;
            }

            System.out.println("Connected to FTP server!");

            System.out.println("*************List file*************");

            if (ftpClient.changeWorkingDirectory(directory)) {
                System.out.println("Changed to directory: " + directory);
            } else {
                System.out.println("Could not change directory.");
            }

            FTPFile[] files = ftpClient.listFiles();

            if (files.length == 0) {
                System.out.println("The FTP server has no files");
            }

            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    System.out.println((i + 1) + ". " + files[i].getName() + " -> " + files[i].getSize() + "bytes");
                }
            }

            System.out.println("Enter the number of the file to download: ");
            int choice = sc.nextInt();

            if (choice < 1 || choice > files.length || !files[choice - 1].isFile()) {
                System.out.println("Invalid selection.");
                return;
            }

            String selectedFile = files[choice - 1].getName();
            System.out.println("Downloading: " + selectedFile);

            FileOutputStream outputStream = new FileOutputStream(selectedFile);
            boolean successFile = ftpClient.retrieveFile(selectedFile, outputStream);

            if (successFile) {
                System.out.println("File downloaded successfully: " + selectedFile);
            } else {
                System.out.println("Failed to download the file.");
            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }

    }
}