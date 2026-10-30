package me.kichyassine.ebankchatbot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EbankChatbotApplicationTests {

    @Autowired
    private Environment environment;

    @Test
    void contextLoads() {
        assertNotNull(environment.getProperty("spring.application.name"));
    }
}
