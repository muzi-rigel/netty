package com.example.demo.bio_multi_thread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {
        int port = 9999;
        ServerSocket serverSocket = null;

        ExecutorService service = Executors.newFixedThreadPool(3);

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server Started...");
            while (true) {
                Socket accept = serverSocket.accept();
//                new Thread(new handler(accept)).start();
                service.execute(new handler(accept));
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

    static class handler implements Runnable {
        Socket socket = null;

        public handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            PrintWriter writer = null;
            BufferedReader reader = null;

            try {
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg = null;

                while (true) {
                    System.out.println(Thread.currentThread().getName() + "server reading...");
                    if ((msg = reader.readLine()) == null) {
                        break;
                    }
                    System.out.println("服务端打印-收到：-->" + msg);
                    writer.println("服务端-收到----->" + msg);
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
