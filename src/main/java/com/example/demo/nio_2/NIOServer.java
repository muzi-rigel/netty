package com.example.demo.nio_2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class NIOServer implements Runnable {

    private Selector selector;
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);


    private NIOServer(int port) {
        init(port);
    }

    public static void main(String[] args) {
        new Thread(new NIOServer(9999)).start();
    }

    private void init(int port) {
        System.out.println("server starting at port " + port);
        try {

            this.selector = Selector.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
            ServerSocketChannel socketChannel = ServerSocketChannel.open();

            socketChannel.configureBlocking(false);
            socketChannel.bind(inetSocketAddress);

            socketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

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
                    System.out.println("---1--- enter");
                    SelectionKey key = keys.next();
                    System.out.println(key);
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
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void read(SelectionKey key) {
        System.out.println("---3--- read");
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

    private void write(SelectionKey key) {
        System.out.println("---4--- write");
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

    private void accept(SelectionKey key) {
        System.out.println("---2--- accept");
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel accept = channel.accept();

            accept.configureBlocking(false);
            accept.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void testModify() {
        System.out.println("---3---");
    }
}
