package priv.stone.demo.springservices.helloconsumer;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Optional;

public class HelloClusterClientInitFunc implements InitFunc {
    private final String remoteAddress = "nacos-headless:8848";
    private final String groupId = "SENTINEL_GROUP";

    private final String flowDataId = "hello-consumer-flow-rules";
//    private final String paramDataId = "PARAM_FLOW_RULE_DATA_ID";

    private final String degradeDataId = "hello-consumer-degrade-rules";

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
        initStateProperty();
    }

    private void initDynamicRuleProperty() {
        ReadableDataSource<String, List<FlowRule>> ruleSource = new NacosDataSource<>(remoteAddress, groupId,
                flowDataId, source -> JSON.parseObject(source, new TypeReference<>() {}));
        FlowRuleManager.register2Property(ruleSource.getProperty());

//        ReadableDataSource<String, List<ParamFlowRule>> paramRuleSource = new NacosDataSource<>(remoteAddress, groupId,
//                paramDataId, source -> JSON.parseObject(source, new TypeReference<>() {}));
//        ParamFlowRuleManager.register2Property(paramRuleSource.getProperty());

        ReadableDataSource<String, List<DegradeRule>> degradeRuleSource = new NacosDataSource<>(remoteAddress, groupId,
                degradeDataId, source -> JSON.parseObject(source, new TypeReference<>() {}));
        DegradeRuleManager.register2Property(degradeRuleSource.getProperty());
    }

    private void initClientConfigProperty() {
        ReadableDataSource<String, ClusterClientConfig> clientConfigDs = new NacosDataSource<>(remoteAddress, groupId,
                configDataId, source -> JSON.parseObject(source, new TypeReference<>() {}));
        ClusterClientConfigManager.registerClientConfigProperty(clientConfigDs.getProperty());
    }

//    private void initClientServerAssignProperty() {
//        ReadableDataSource<String, ClusterClientAssignConfig> clientAssignDs = new NacosDataSource<>(remoteAddress, groupId,
//                clusterMapDataId, source -> {
//            List<ClusterClientAssignConfig> assignConfigs = JSON.parseObject(source, new TypeReference<>() {});
//            return (assignConfigs == null || assignConfigs.size() == 0) ? null : assignConfigs.get(0);
//        });
//        ClusterClientConfigManager.registerServerAssignProperty(clientAssignDs.getProperty());
//    }
    private void initClientServerAssignProperty() {
        ReadableDataSource<String, ClusterClientAssignConfig> clientAssignDs = new NacosDataSource<>(remoteAddress, groupId,
                clusterMapDataId, source -> {
            List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<>() {});
            return Optional.ofNullable(groupList)
                    .flatMap(this::extractClientAssignment)
                    .orElse(null);
        });
        ClusterClientConfigManager.registerServerAssignProperty(clientAssignDs.getProperty());
    }

    private void initStateProperty() {
        // Cluster map format:
        // [{"clientSet":["112.12.88.66@8729","112.12.88.67@8727"],"ip":"112.12.88.68","machineId":"112.12.88.68@8728","port":11111}]
        // machineId: <ip@commandPort>, commandPort for port exposed to Sentinel dashboard (transport module)
        ReadableDataSource<String, Integer> clusterModeDs = new NacosDataSource<>(remoteAddress, groupId,
                clusterMapDataId, source -> {
            List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<>() {});
            return Optional.ofNullable(groupList)
                    .map(this::extractMode)
                    .orElse(ClusterStateManager.CLUSTER_NOT_STARTED);
        });
        ClusterStateManager.registerProperty(clusterModeDs.getProperty());
    }

    private int extractMode(List<ClusterGroupEntity> groupList) {
        return ClusterStateManager.CLUSTER_CLIENT;
    }

    private Optional<ClusterClientAssignConfig> extractClientAssignment(List<ClusterGroupEntity> groupList) {
        if (groupList.stream().anyMatch(this::machineEqual)) {
            return Optional.empty();
        }
        // Build client assign config from the client set of target server group.
        for (ClusterGroupEntity group : groupList) {
            if (group.getClientSet().contains(getCurrentMachineId())) {
                String ip = group.getIp();
                Integer port = group.getPort();
                return Optional.of(new ClusterClientAssignConfig(ip, port));
            }
        }
        return Optional.empty();
    }

    private boolean machineEqual(/*@Valid*/ ClusterGroupEntity group) {
        return getCurrentMachineId().equals(group.getMachineId());
    }

    private String getCurrentMachineId() {
        // Note: this may not work well for container-based env.
        return HostNameUtil.getIp() + SEPARATOR + TransportConfig.getRuntimePort();
    }

    private static final String SEPARATOR = "@";

}
