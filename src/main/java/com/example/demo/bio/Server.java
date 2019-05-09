package com.example.demo.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {


    public static void main(String[] args) {
        int port = 9999;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server Started");

            while (true) {
                System.out.println("while true in ");
                Socket socket = serverSocket.accept();
                System.out.println("accept success ");
                new Thread(new Handler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    System.out.println("The serverSocket is over");
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            serverSocket = null;
        }

    }


    static class Handler implements Runnable {

        Socket socket = null;

        public Handler(Socket socket) {
            System.out.println("runnable construct");
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            PrintWriter writer = null;

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

                String msg = null;

                while (true) {
                    System.out.println(Thread.currentThread().getName() + "server reading...");
                    if ((msg = reader.readLine()) == null) {
                        break;
                    }
                    System.out.println("---:" + msg);
                    writer.println("这边是服务器端，收到信息:" + msg);
                    writer.flush();
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
}
