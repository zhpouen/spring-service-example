package priv.stone.demo.springservices.helloprovider.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hello-provider-boot", path = "/")
public interface HelloService {
    @GetMapping("/echo/{string}")
    String echo(@PathVariable("string") String string);

    @GetMapping("/divide")
    public String divide(@RequestParam Integer a, @RequestParam Integer b);
}
