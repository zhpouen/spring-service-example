package priv.stone.demo.springservices.helloconsumer;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
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

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloClientApplication.class, args);
    }

}
