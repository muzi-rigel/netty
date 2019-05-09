package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {

        String a1 = "abc";
        String a2 = "木子里";

        System.out.println(a1.getBytes());
        System.out.println(Arrays.toString(a1.getBytes()));
        System.out.println(a2);
        System.out.println(a2.getBytes());
        System.out.println(a2.getBytes(StandardCharsets.UTF_8));

    }

}
