package priv.stone.demo.springservices.helloconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import priv.stone.demo.springservices.helloprovider.api.HelloService;

@RestController
public class HelloClientController {
    @Autowired
    private HelloService helloService;

    @GetMapping("/echo/{string}")
    public String echo(@PathVariable String string) {
        return "consumer ==> " + helloService.echo(string);
    }

    @GetMapping("/divide")
    public String divide(@RequestParam Integer a, @RequestParam Integer b) {
        return "consumer ==> " + helloService.divide(a, b);
    }

}
