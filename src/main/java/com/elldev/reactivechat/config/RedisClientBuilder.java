package com.elldev.reactivechat.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.BaseConfig;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

public class RedisClientBuilder {

    private static void setPassword(BaseConfig config, String password) {
        if (password != null) {
            config.setPassword(password);
        }
    }

    public static RedissonClient buildRedissonClient(RedisConfig rc) {
        Config config = new Config().setCodec(StringCodec.INSTANCE);

        if (rc.getMode().equals("single")) {
            SingleServerConfig ssc = config.useSingleServer();
            ssc.setAddress(rc.getNodes().get(0));
            ssc.setTimeout(rc.getTimeout());
            setPassword(ssc, rc.getPassword());
            return Redisson.create(config);
        } else if (rc.getMode().equals("cluster")) {
            ClusterServersConfig csc = config.useClusterServers();
            rc.getNodes().forEach(node -> {
                csc.addNodeAddress(node);
            });
            csc.setTimeout(rc.getTimeout());
            setPassword(csc, rc.getPassword());
            return Redisson.create(config);
        }
        throw new IllegalArgumentException("Invalid mode: " + rc.getMode());
    }
}
