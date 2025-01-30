package com.xulio.gal;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SSLGuessGameClient {
    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 60000);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            var sc = new Scanner(System.in);

            while (true) {
                System.out.println(in.nextLine());

                String command = sc.nextLine();

                out.println(command);
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}