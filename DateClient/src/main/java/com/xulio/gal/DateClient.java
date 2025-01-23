package com.xulio.gal;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class DateClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        var socket = new Socket(args[0], 57778);
        var in = new Scanner(socket.getInputStream());
        System.out.println("Server response: " + in.nextLine());

        Scanner inKey = new Scanner(System.in);
        inKey.nextLine();
    }
}
