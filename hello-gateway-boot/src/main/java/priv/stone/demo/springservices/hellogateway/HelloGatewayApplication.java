package priv.stone.demo.springservices.hellogateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HelloGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloGatewayApplication.class, args);
    }
}
