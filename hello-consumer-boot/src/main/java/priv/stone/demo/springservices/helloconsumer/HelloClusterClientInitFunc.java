package priv.stone.demo.springservices.helloconsumer;

import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class HelloClusterClientInitFunc implements InitFunc {
    private final String remoteAddress = "nacos-headless:8848";
    private final String groupId = "SENTINEL_GROUP";

    private final String flowDataId = "hello-consumer-flow-rules";
//    private final String paramDataId = "PARAM_FLOW_RULE_DATA_ID";
    private final String configDataId = "hello_cluster_client_config_data_id";

    private final String clusterMapDataId = "hello_cluster_map_data_id";

    @Override
    public void init() throws Exception {
        // Register client dynamic rule data source.
        initDynamicRuleProperty();

        // Register token client related data source.
        // Token client common config:
        initClientConfigProperty();
        // Token client assign config (e.g. target token server) retrieved from assign map:
        initClientServerAssignProperty();
    }

    private void initDynamicRuleProperty() {
        ReadableDataSource<String, List<FlowRule>> ruleSource = new NacosDataSource<>(remoteAddress, groupId,
                flowDataId, source -> JSON.parseObject(source, new TypeReference<>() {}));
        FlowRuleManager.register2Property(ruleSource.getProperty());

//        ReadableDataSource<String, List<ParamFlowRule>> paramRuleSource = new NacosDataSource<>(remoteAddress, groupId,
//                paramDataId, source -> JSON.parseObject(source, new TypeReference<>() {}));
//        ParamFlowRuleManager.register2Property(paramRuleSource.getProperty());
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
//private void initClientServerAssignProperty() {
//    ReadableDataSource<String, List<ClusterClientAssignConfig>> clientAssignDs = new NacosDataSource<>(remoteAddress, groupId,
//            configDataId, source -> {
//        List<ClusterClientAssignConfig> assignConfigs = JSON.parseObject(source, new TypeReference<List<ClusterClientAssignConfig>>() {});
//        return Optional.ofNullable(assignConfigs).orElse(null);
//    });
//    DataSource<List<ClusterClientAssignConfig>> dataSource = new ConverterDataSource<>(clientAssignDs, new ClusterClientAssignConfigConverter());
//    ClusterClientConfigManager.registerServerAssignProperty(dataSource);
//}

}
