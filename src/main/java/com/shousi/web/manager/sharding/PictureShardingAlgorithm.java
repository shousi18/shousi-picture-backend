package com.shousi.web.manager.sharding;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;

/**
 * 自定义图片分片算法
 */
public class PictureShardingAlgorithm implements StandardShardingAlgorithm<Long> {

    /**
     * 图片分表逻辑算法实现
     * @param collection 表集合
     * @param preciseShardingValue 要查询的表
     * @return
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        Long spaceId = preciseShardingValue.getValue();
        // 逻辑表
        String logicTableName = preciseShardingValue.getLogicTableName();
        // spaceId为null表示查询所有图片
        if (spaceId == null) {
            return logicTableName;
        }
        // 根据spaceId动态生成分表名
        String realTableName = "picture_" + spaceId;
        if (collection.contains(realTableName)) {
            // 如果表集合中包括该表名，则返回该表名
            return realTableName;
        }else {
            return logicTableName;
        }
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        return null;
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties properties) {

    }
}
