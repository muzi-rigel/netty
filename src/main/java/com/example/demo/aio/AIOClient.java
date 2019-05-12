package com.example.demo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AIOClient {

    private AsynchronousSocketChannel channel;

    public AIOClient(String host, int port) {
        init(host, port);
    }

    private void init(String host, int port) {
        try {
            channel = AsynchronousSocketChannel.open();

            channel.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String line){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(line.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            channel.write(buffer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void read(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            channel.read(buffer).get();
            buffer.flip();
            System.out.println("from server :" + new String(buffer.array(), StandardCharsets.UTF_8));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void doDestroy(){
        if(null != channel){
            try {
                channel.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        AIOClient client = new AIOClient("localhost", 9999);
        try {
            System.out.println("enter message send to server > ");
            Scanner s = new Scanner(System.in);
            String line = s.nextLine();
            client.write(line);
            client.read();
        } finally {
            client.doDestroy();
        }

    }
}
