package com.hallo.mall;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallApplicationTests {

    @Test
    public void load() {

    }

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Test
    public void sendMQMsg() {

        amqpTemplate.convertAndSend("payNotify", "hello");
    }

}

