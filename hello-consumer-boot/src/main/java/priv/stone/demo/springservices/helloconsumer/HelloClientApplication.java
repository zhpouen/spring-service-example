package priv.stone.demo.springservices.helloconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HelloClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloClientApplication.class, args);
    }

}
