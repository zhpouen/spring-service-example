package priv.stone.demo.springservices.helloconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import priv.stone.demo.springservices.helloprovider.api.HelloService;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = HelloService.class)
public class HelloClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloClientApplication.class, args);
    }

}
