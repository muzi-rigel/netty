package com.example.demo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AIOServer {

    private ExecutorService service;

    //服务的通道
    private AsynchronousServerSocketChannel serverChannel;

    public AIOServer(int port) {
        init(9999);
    }

    private void init(int port) {
        try {
            System.out.println("server starting at port: " + port + "...");

            service = Executors.newFixedThreadPool(4);

            serverChannel = AsynchronousServerSocketChannel.open();

            serverChannel.bind(new InetSocketAddress(port));

            System.out.println("server started .");

            //开始监听
            serverChannel.accept(this, new AIOServerHandler());

            try {
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ExecutorService getService() {
        return service;
    }

    public void setService(ExecutorService service) {
        this.service = service;
    }

    public AsynchronousServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(AsynchronousServerSocketChannel serverChannel) {
        this.serverChannel = serverChannel;
    }

    public static void main(String[] args) {
        new AIOServer(9999);
    }
}
