package com.example.demo.bio_multi_thread;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String ip = "localhost";
        int port = 9999;

        PrintWriter writer = null;
        BufferedReader reader = null;

        Socket socket = null;

        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket(ip, port);
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String msg = null;
            while (true) {
                msg = scanner.nextLine();
                if("exit".equalsIgnoreCase(msg)){
                    break;
                }
                writer.println("客户端录入:-->" + msg);
                writer.flush();
                msg = reader.readLine();
                System.out.println("客户端打印：--->" + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket = null;
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            reader = null;
            if (writer != null) {
                writer.close();

            }
            writer = null;
        }
    }
}
