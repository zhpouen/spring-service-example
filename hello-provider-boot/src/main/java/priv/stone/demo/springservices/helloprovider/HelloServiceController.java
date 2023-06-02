package priv.stone.demo.springservices.helloprovider;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class HelloServiceController {
    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @PostConstruct
    private void init() {
        List<FlowRule> rules = new ArrayList<>();
        // 创建流控规则并配置
        FlowRule rule1 = new FlowRule();
        rule1.setResource("/echo/{string}");
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setCount(10);
        rules.add(rule1);

        FlowRule rule2 = new FlowRule();
        rule2.setResource("/divide");
        rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule2.setCount(5);
        rules.add(rule2);

        // 将规则加载到 Sentinel 的规则管理器中
        FlowRuleManager.loadRules(rules);
    }

    private String handleEchoBlock(String string, BlockException ex) {
        return "Blocked for echo: " + string;
    }

    private String handleDivideBlock(Integer a, Integer b, BlockException ex) {
        return "Blocked for divide: " + a + " / " + b;
    }

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return new ResponseEntity<>("index error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/sleep")
    public String sleep() {
        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @GetMapping("/echo/{string}")
    @SentinelResource(value = "echo", blockHandler = "handleEchoBlock")
    public String echo(@PathVariable String string) {
        return "hello Nacos Discovery " + string;
    }

    @GetMapping("/divide")
    @SentinelResource(value = "divide", blockHandler = "handleDivideBlock")
    public String divide(@RequestParam Integer a, @RequestParam Integer b) {
        if (b == 0) {
            return String.valueOf(0);
        }
        else {
            return String.valueOf(a / b);
        }
    }

    @GetMapping("/zone")
    public String zone() {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        return "provider zone " + metadata.get("zone");
    }
}
