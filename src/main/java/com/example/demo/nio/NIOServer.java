package com.example.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class NIOServer implements Runnable {

    //多路复用器
    private Selector selector;
    //读写缓存
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);


    public static void main(String[] args) {
        new Thread(new NIOServer(9999)).start();
    }

    private NIOServer(int port) {
        init(port);
    }

    private void init(int port) {

        System.out.println("server starting at port " + port);
        try {
            this.selector = Selector.open();

            //开启服务通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            //非阻塞
            serverSocketChannel.configureBlocking(false);

            //绑定端口
            serverSocketChannel.bind(new InetSocketAddress(port));

            //注册并标记当前服务通道状态
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            System.out.println("server started.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {

                this.selector.select();

                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isValid()) {
                        //阻塞状态
                        try {
                            if (key.isAcceptable()) {
                                accept(key);
                            }
                        } catch (CancelledKeyException e) {
                            key.cancel();
                        }
                        //可读状态
                        try {
                            if (key.isReadable()) {
                                read(key);
                            }
                        } catch (CancelledKeyException e) {
                            key.cancel();
                        }
                        //可写状态
                        try {
                            if (key.isWritable()) {
                                write(key);
                            }
                        } catch (CancelledKeyException e) {
                            key.cancel();
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void write(SelectionKey key) {

        this.writeBuffer.clear();
        SocketChannel channel = (SocketChannel) key.channel();

        Scanner reader = new Scanner(System.in);

        try {
            System.out.print("put message for send to client > ");
            String line = reader.nextLine();
            writeBuffer.put(line.getBytes(StandardCharsets.UTF_8));

            writeBuffer.flip();

            channel.write(writeBuffer);

            channel.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                key.channel().close();
                key.cancel();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void read(SelectionKey key) {

        this.readBuffer.clear();
        SocketChannel channel = (SocketChannel) key.channel();

        try {

            int readLength = channel.read(readBuffer);
            if (readLength == -1) {
                key.channel().close();
                key.cancel();
                return;
            }
            // flip重置游标
            this.readBuffer.flip();
            byte[] datas = new byte[readBuffer.remaining()];
            readBuffer.get(datas);
            System.out.println("from " + channel.getRemoteAddress() + " client： "
                    + new String(datas, StandardCharsets.UTF_8));

            channel.register(this.selector, SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                key.channel().close();
                key.cancel();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) {

        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel channel = serverChannel.accept();

            channel.configureBlocking(false);

            channel.register(this.selector, SelectionKey.OP_READ);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
