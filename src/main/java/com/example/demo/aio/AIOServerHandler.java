package com.example.demo.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AIOServerHandler implements CompletionHandler<AsynchronousSocketChannel, AIOServer> {
    @Override
    public void completed(AsynchronousSocketChannel result, AIOServer attachment) {
        attachment.getServerChannel().accept(attachment, this);
        doRead(result);
    }

    private void doRead(final AsynchronousSocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println(attachment.capacity());
                attachment.flip();
                System.out.println("from client : " + new String(attachment.array(), StandardCharsets.UTF_8));
                doWrite(channel);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer byteBuffer) {
                exc.printStackTrace();
            }
        });
    }

    private void doWrite(AsynchronousSocketChannel result) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            System.out.println("enter message send to client > ");
            Scanner s = new Scanner(System.in);
            buffer.put(s.nextLine().getBytes());
            buffer.flip();
            result.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, AIOServer attachment) {
        exc.printStackTrace();
    }
}
