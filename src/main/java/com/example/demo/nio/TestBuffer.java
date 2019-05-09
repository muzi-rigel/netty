package com.example.demo.nio;

import java.nio.ByteBuffer;

public class TestBuffer {

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(8);

        System.out.println(buffer);
        buffer.put(new byte[]{4, 3, 2});
        System.out.println(buffer);

        buffer.flip();
        System.out.println(buffer);


        for (int i = 0; i < 3; i++) {
            byte b = buffer.get(i);
            System.out.println("i->" + b);
        }

        System.out.println(buffer);
        buffer.clear();

        System.out.println(buffer);

    }

}
