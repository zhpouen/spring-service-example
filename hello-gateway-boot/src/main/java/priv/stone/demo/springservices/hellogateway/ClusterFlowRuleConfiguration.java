/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package priv.stone.demo.springservices.hellogateway;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * @author Eric Zhao
 */
//@Configuration
public class ClusterFlowRuleConfiguration {

    private final String remoteAddress = "nacos-headless:8848";
    private final String groupId = "SENTINEL_GROUP";

//    private final String flowDataId = "hello-consumer-flow-rules";
    //    private final String paramDataId = "PARAM_FLOW_RULE_DATA_ID";
    private final String configDataId = "hello_cluster_client_config_data_id";

    private final String clusterMapDataId = "hello_cluster_map_data_id";

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public ClusterFlowRuleConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                        ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // Register the block exception handler for Spring Cloud Gateway.
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void doInit() {
//        initCustomizedApis();
//        initGatewayRules();

        // Register token client related data source.
        // Token client common config:
        initClientConfigProperty();
        // Token client assign config (e.g. target token server) retrieved from assign map:
        initClientServerAssignProperty();
    }

    private void initClientConfigProperty() {
        ReadableDataSource<String, ClusterClientConfig> clientConfigDs = new NacosDataSource<>(remoteAddress, groupId,
                configDataId, source -> JSON.parseObject(source, new TypeReference<>() {}));
        ClusterClientConfigManager.registerClientConfigProperty(clientConfigDs.getProperty());
    }

    private void initClientServerAssignProperty() {
        ReadableDataSource<String, ClusterClientAssignConfig> clientAssignDs = new NacosDataSource<>(remoteAddress, groupId,
                clusterMapDataId, source -> {
            List<ClusterClientAssignConfig> assignConfigs = JSON.parseObject(source, new TypeReference<>() {});
            return (assignConfigs == null || assignConfigs.size() == 0) ? null : assignConfigs.get(0);
        });
        ClusterClientConfigManager.registerServerAssignProperty(clientAssignDs.getProperty());
    }

//    private void initCustomizedApis() {
//        Set<ApiDefinition> definitions = new HashSet<>();
//        ApiDefinition api1 = new ApiDefinition("some_customized_api")
//            .setPredicateItems(new HashSet<ApiPredicateItem>() {{
//                add(new ApiPathPredicateItem().setPattern("/ahas"));
//                add(new ApiPathPredicateItem().setPattern("/product/**")
//                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
//            }});
//        ApiDefinition api2 = new ApiDefinition("another_customized_api")
//            .setPredicateItems(new HashSet<ApiPredicateItem>() {{
//                add(new ApiPathPredicateItem().setPattern("/**")
//                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
//            }});
//        definitions.add(api1);
//        definitions.add(api2);
//        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
//    }

//    private void initGatewayRules() {
//        Set<GatewayFlowRule> rules = new HashSet<>();
//        rules.add(new GatewayFlowRule("aliyun_route")
//            .setCount(10)
//            .setIntervalSec(1)
//        );
//        rules.add(new GatewayFlowRule("aliyun_route")
//            .setCount(2)
//            .setIntervalSec(2)
//            .setBurst(2)
//            .setParamItem(new GatewayParamFlowItem()
//                .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)
//            )
//        );
//        rules.add(new GatewayFlowRule("httpbin_route")
//            .setCount(10)
//            .setIntervalSec(1)
//            .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
//            .setMaxQueueingTimeoutMs(600)
//            .setParamItem(new GatewayParamFlowItem()
//                .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_HEADER)
//                .setFieldName("X-Sentinel-Flag")
//            )
//        );
//        rules.add(new GatewayFlowRule("httpbin_route")
//            .setCount(1)
//            .setIntervalSec(1)
//            .setParamItem(new GatewayParamFlowItem()
//                .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
//                .setFieldName("pa")
//            )
//        );
//        rules.add(new GatewayFlowRule("httpbin_route")
//            .setCount(2)
//            .setIntervalSec(30)
//            .setParamItem(new GatewayParamFlowItem()
//                .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
//                .setFieldName("type")
//                .setPattern("warn")
//                .setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_CONTAINS)
//            )
//        );
//
//        rules.add(new GatewayFlowRule("some_customized_api")
//            .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
//            .setCount(5)
//            .setIntervalSec(1)
//            .setParamItem(new GatewayParamFlowItem()
//                .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
//                .setFieldName("pn")
//            )
//        );
//        GatewayRuleManager.loadRules(rules);
//    }
}
