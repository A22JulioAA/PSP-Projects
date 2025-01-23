package com.xulio.gal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class PowClient {
    public static void main(String[] args) {
        Socket socket = null;
        PrintWriter out = null;
        Scanner in = null;

        try {
            socket = new Socket("localhost", 57777);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            var sc = new Scanner(System.in);

            System.out.println(in.nextLine());

            System.out.print("Enter a number: ");
            var number = sc.nextInt();

            out.println(number);

            System.out.println(in.nextLine());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error closing socket: " + e.getMessage());
                }
            }
        }
    }
}
