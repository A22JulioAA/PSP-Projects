package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jcraft.jsch.*;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class DownloadSFTP {
    private static final String HOST = "192.168.114.1";
    private static final int PORT = 60000;
    private static final String USERNAME = "tester";
    private static final String API_URL = "https://picsum.photos/v2/list?page=1&limit=5";
    private static final String REMOTE_DIR = "";
    private static final String privateKeyPath = "C:\\Users\\julio\\OneDrive\\Documentos\\" +
            "2DAM\\PSP\\PSP-Projects-1\\id_rsa";

    public static Session getJSchSession () {
        JSch jSch = new JSch();

        Session session = null;

        try {
            jSch.addIdentity(privateKeyPath);
            session = jSch.getSession(USERNAME, HOST, PORT);
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }

        return session;
    }

    public static ChannelSftp getChannel (Session session) {
        ChannelSftp channelSftp = null;

        try {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }

        return channelSftp;
    }

    public static List<String> fetchImageURLs (String apiUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            List<JsonObject> images = new Gson().fromJson(reader, new TypeToken<List<JsonObject>>() {}.getType());

            reader.close();

            return images.stream()
                    .map(json -> json.get("download_url").getAsString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void downloadImage (String imageUrl, String localFilePath) throws IOException {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .GET()
                .build();

        try {
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() == 200) {
                Files.copy(response.body(), Path.of(localFilePath));
                System.out.println("Imagen descargada: " + localFilePath);
            } else {
                throw new IOException("Error HTTP: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al descargar la imagen: " + e.getMessage(), e);
        }
    }

    public static void uploadImage (Session session, ChannelSftp channelSftp, String localFilePath, String remoteDir) {
        try (
                FileInputStream fis = new FileInputStream(new File(localFilePath));
                ) {
            channelSftp.put(fis, remoteDir + "/" + new File(localFilePath).getName());

            System.out.println("Archivo subido con éxito! " + localFilePath);
        } catch (IOException | SftpException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SftpException, IOException {
        Session session = getJSchSession();
        ChannelSftp channelSftp = getChannel(session);

        List<String> images = fetchImageURLs(API_URL);

        for (int i = 0; i < images.size(); i++) {
            String imageUrl = images.get(i);
            String savePath = "image_" + i + ".jpg";
            downloadImage(imageUrl, savePath);

            uploadImage(session, channelSftp, savePath, REMOTE_DIR);
        }

        channelSftp.disconnect();
        session.disconnect();
    }
}