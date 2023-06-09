package priv.stone.demo.springservices.hellogateway;

import com.alibaba.nacos.client.naming.utils.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
class SpringServicesApplicationTests {

	@Test
	public void testConcurrentRequests() throws InterruptedException {
		String baseUrl = "http://localhost:8080/api/v1/namespaces/default/services/hello-gateway-boot-service:30002/proxy/consumer/echo/helloConsumerGateway";
		int numThreads = 10;
		int numRequestsPerThread = 10;

		ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

		for (int i = 0; i < numThreads; i++) {
			executorService.submit(() -> {
				for (int j = 0; j < numRequestsPerThread; j++) {
					makeRequest(baseUrl + RandomUtils.nextInt(10000));
				}
			});
		}

		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.SECONDS);
	}

	private void makeRequest(String url) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		System.out.println("Response status: " + response.getStatusCode());
		System.out.println("Response body: " + response.getBody());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
