<<<<<<<< HEAD:configuration-spring-api/src/test/java/io/naryo/api/TestSpringApplication.java
package io.naryo.api;
========
package io.naryo;
>>>>>>>> de348658 (chore: Implement API for creating store configurations):configuration-spring-api/src/test/java/io/naryo/TestSpringApplication.java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestSpringApplication.class, args);
    }
}
