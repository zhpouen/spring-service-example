package priv.stone.demo.springservices.echoprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class EchoServiceController {
    @Value("${echo.local}")
    private String echoLocal;

    @Value("${echo.remote}")
    private String echo;

    @Value("${echo.replace}")
    private String echoReplace;

    @Autowired
    private EchoConfigInfo echoConfigInfo;

    @GetMapping("/echo-local/{string}")
    public String echoLocal(@PathVariable String string) {
        return echoLocal + " Nacos Discovery " + string;
    }

    @GetMapping("/echo-replace/{string}")
    public String echoReplace(@PathVariable String string) {
        return echoReplace + " Nacos Discovery " + string;
    }

    @GetMapping("/echo/{string}")
    public String echo(@PathVariable String string) {
        return echo + " Nacos Discovery " + string;
    }

    @GetMapping("/divide")
    public String divide(@RequestParam Integer a, @RequestParam Integer b) {

        if (b == 0) {
            return echoConfigInfo.getMessage();
        }
        else {
            return echoConfigInfo.getMessage() + " " + (a / b);
        }
    }

}
