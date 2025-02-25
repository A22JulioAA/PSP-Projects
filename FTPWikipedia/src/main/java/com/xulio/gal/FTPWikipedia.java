package com.xulio.gal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FTPWikipedia {
    public static void showServerReplay (FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();

        if (replies != null && replies.length > 0) {
            for (String aReplay : replies) {
                System.out.println("SERVER: " + aReplay);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String server = "10.26.0.32";
        int port = 21;
        String user = "julio";
        String password = "julio";

        Scanner sc = new Scanner(System.in);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        System.out.println("Introduce the country code(ES, US...): ");
        String countryCode = sc.nextLine().trim();

        System.out.println("Introduce the year: ");
        String year = sc.nextLine().trim();

        System.out.println("Introduce the month(01, 02...): ");
        String month = sc.nextLine().trim();

        System.out.println("Introduce the day: ");
        String day = sc.nextLine().trim();

        String wikipediaUrl = "https://wikimedia.org/api/rest_v1/metrics/pageviews/top-per-country/"
                + countryCode
                + "/all-access/"
                + year
                + "/"
                + month
                + "/"
                + day;

        String filename = countryCode + "-" + year + month + day + ".json";

        URL url = new URL(wikipediaUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error: HTTP " + conn.getResponseCode());
        }

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
                ) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            System.out.println(gson.fromJson(jsonObject, JsonObject.class));

            bw.write(gson.toJson(jsonObject));
            bw.flush();
        } finally {
            conn.disconnect();
        }

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);

            showServerReplay(ftpClient);

            int replyCode = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return;
            }

            boolean success = ftpClient.login(user, password);

            showServerReplay(ftpClient);

            if (!success) {
                System.out.println("Could not login to the server");
                return;
            }

            System.out.println("Connected to FTP server.");

            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            try (
                    FileInputStream inputStream = new FileInputStream(filename)
                    ) {
                boolean uploaded = ftpClient.storeFile(filename, inputStream);

                if (uploaded) {
                    System.out.println("File uploades succesfully!");
                } else {
                    System.out.println("Something went wrong uploading the file...");
                }

                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            System.out.println("Something wrong happened");
        }
    }
}