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
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadFTP {
    private static final Logger logger = Logger.getLogger(DownloadFTP.class.getName());

    private static final String SERVER = "ftp.scene.org";
    private static final int PORT = 21;
    private static final String USER = "anonymous";
    private static final String DIRECTORY = "/pub";

    public static void showServerReplay (FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();

        if (replies != null && replies.length > 0) {
            for (String aReplay : replies) {
                System.out.println("SERVER: " + aReplay);
            }
        }
    }

    public static FTPClient connectAndLogin () throws IOException {
        FTPClient ftpClient = new FTPClient();

        ftpClient.connect(SERVER, PORT);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();

        showServerReplay(ftpClient);

        int replyCode = ftpClient.getReplyCode();

        if (!FTPReply.isPositiveCompletion(replyCode)) {
            throw new IOException("Connection failed. Server reply code: " + replyCode);
        }

        boolean success = ftpClient.login(USER, "");
        showServerReplay(ftpClient);

        if (!success) {
            throw new IOException("Could not log in to the server.");
        }

        System.out.println("Connected to FTP Server!");
        return ftpClient;
    }

    public static FTPFile[] listFiles (FTPClient ftpClient) throws IOException {
        if (!ftpClient.changeWorkingDirectory(DIRECTORY)) {
            throw new IOException("Could not change directory to: " + DIRECTORY);
        }

        System.out.println("************* List of Files *************");

        FTPFile[] files = ftpClient.listFiles();

        if (files.length == 0) {
            throw new IOException("No files found on the FTP server.");
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                System.out.println((i + 1) + ". " + files[i].getName() + " -> " + files[i].getSize() + "bytes");
            }
        }

        return files;
    }

    public static void downloadFile (FTPClient ftpClient, FTPFile file) throws IOException {
        System.out.println("Downloading: " + file.getName());

        try (
                FileOutputStream outputStream = new FileOutputStream(file.getName());
                ) {
            if (ftpClient.retrieveFile(file.getName(), outputStream)) {
                System.out.println("File downloaded successfully: " + file.getName());
            } else {
                throw new IOException("Failed to download the file.");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // Logger to show errors in console
        logger.setLevel(Level.INFO);

        try (
                Scanner sc = new Scanner(System.in);
                ) {
            FTPClient ftpClient = connectAndLogin();

            try {
                FTPFile[] files = listFiles(ftpClient);

                System.out.println("Enter the number of the file to download: ");
                int choice;

                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                } else {
                    throw new IllegalArgumentException("Invalid input. Please enter a number.");
                }

                if (choice < 1 || choice > files.length || !files[choice - 1].isFile()) {
                    throw new IllegalArgumentException("Invalid selection.");
                }

                downloadFile(ftpClient, files[choice - 1]);
            } finally {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (SocketException e) {
            System.err.println("Socket error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("User input error: " + e.getMessage());
        }

    }
}