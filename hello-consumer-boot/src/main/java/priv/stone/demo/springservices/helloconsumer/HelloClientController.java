package priv.stone.demo.springservices.helloconsumer;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import priv.stone.demo.springservices.helloprovider.api.HelloService;

@RestController
@Slf4j
public class HelloClientController {
    @Autowired
    private HelloService helloService;

    private String handleEchoBlock(String string, BlockException ex) {
        log.info("Consumer Blocked for echo", ex);
        return "Consumer Blocked for echo: " + string;
    }

    private String handleDivideBlock(Integer a, Integer b, BlockException ex) {
        log.info("Consumer Blocked for divide", ex);
        return "Consumer Blocked for divide: " + a + " / " + b;
    }

    private String handleDivideDegrade(Integer a, Integer b, BlockException ex) {
        log.info("Consumer Degraded for divide", ex);
        return "Consumer Degraded for divide: " + a + " / " + b;
    }

    @GetMapping("/echo/{string}")
    @SentinelResource(value = "echo", blockHandler = "handleEchoBlock")
    public String echo(@PathVariable String string) {
        return "consumer ==> " + helloService.echo(string);
    }

    @GetMapping("/divide")
    @SentinelResource(value = "divide", blockHandler = "handleDivideBlock", fallback = "handleDivideDegrade")
    public String divide(@RequestParam Integer a, @RequestParam Integer b) {
        return "consumer ==> " + helloService.divide(a, b);
    }

}
