package com.xulio.gal;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class PowClient {
    public static void main(String[] args) {
        try {
            var socket = new Socket("localhost", 57777);
            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);
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
        }

    }
}
