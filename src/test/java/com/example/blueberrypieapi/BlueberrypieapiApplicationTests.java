package com.example.blueberrypieapi;

import com.example.blueberrypieapi.service.RoleService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.time.LocalDateTime;

@SpringBootTest
class BlueberrypieapiApplicationTests {

    @Autowired
    JavaMailSenderImpl mailSender;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("标题");
        message.setTo("15913008007@163.com");
        message.setFrom("2546663958@qq.com");
        message.setText("good");
        mailSender.send(message);
    }

    @Test
    void setAmqpAdminTest(){
//        rabbitTemplate.convertAndSend("mail.exchange","MAIL_KEY","good");

//        amqpAdmin.declareExchange(new DirectExchange("mail.exchange"));
//        amqpAdmin.declareQueue(new Queue("MAIL",true));
//        amqpAdmin.declareBinding(new Binding("MAIL",
//                Binding.DestinationType.QUEUE,"mail.exchange","MAIL_KEY",null));

    }

    @Test
    void testt(){
        System.out.println(LocalDateTime.now());
    }

}
