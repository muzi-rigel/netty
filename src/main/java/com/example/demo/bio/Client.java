package com.example.demo.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

//        1 获取socket连接
//        2 发送数据

        String host = "127.0.0.1";
        int port = 9999;

        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;

        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket(host, port);
            String message = null;

            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            writer = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                message = scanner.nextLine();
                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }
                writer.println(message);
                writer.flush();
                System.out.println("send-" + message);
                System.out.println("reader readline:"+reader.readLine());

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
