package priv.stone.demo.springservices.helloflowserver;

import com.alibaba.csp.sentinel.cluster.server.SentinelDefaultTokenServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloFlowServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloFlowServerApplication.class, args);
    }

    @Bean
    public ClusterTokenServer clusterTokenServer() {
        return new ClusterTokenServer();
    }

    public static class ClusterTokenServer implements CommandLineRunner {

        @Override
        public void run(String... args) throws Exception {
            // Not embedded mode by default (alone mode).
            SentinelDefaultTokenServer tokenServer = new SentinelDefaultTokenServer();
            // Start the server.
            tokenServer.start();
        }
    }
}
