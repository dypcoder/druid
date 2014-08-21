package com.alibaba.druid.bvt.pool;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.druid.pool.DruidDataSource;

public class CreateSchedulerTest extends TestCase {

    private DruidDataSource          dataSource;
    private ScheduledExecutorService createScheduler;

    protected void setUp() throws Exception {
        createScheduler = Executors.newScheduledThreadPool(1);

        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mock:xxx");
        dataSource.setDestroyScheduler(createScheduler);
        dataSource.setMinEvictableIdleTimeMillis(10);
        dataSource.setTimeBetweenEvictionRunsMillis(10);
    }

    protected void tearDown() throws Exception {
        dataSource.close();
        createScheduler.shutdown();
    }

    public void test_connectAndClose() throws Exception {
        Connection[] connections = new Connection[8];
        for (int i = 0; i < connections.length; ++i) {
            connections[i] = dataSource.getConnection();
        }
        
        for (int i = 0; i < connections.length; ++i) {
            connections[i].close();
        }
        
        Thread.sleep(100);
        Assert.assertEquals(0, dataSource.getPoolingCount());
    }
}
