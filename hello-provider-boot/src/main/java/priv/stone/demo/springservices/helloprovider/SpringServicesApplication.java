package priv.stone.demo.springservices.helloprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringServicesApplication.class, args);
	}

}
