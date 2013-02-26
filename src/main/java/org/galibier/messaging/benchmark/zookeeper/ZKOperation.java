package org.galibier.messaging.benchmark.zookeeper;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryOneTime;
import org.apache.zookeeper.KeeperException;
import org.galibier.messaging.benchmark.Message;
import org.galibier.messaging.benchmark.Operation;

public abstract class ZKOperation implements Operation {
    protected final CuratorFramework client;
    protected final String path;

    public ZKOperation(String host, String path) {
        this.client = CuratorFrameworkFactory.newClient(host, new RetryOneTime(100));
        this.path = path;
    }

    @Override
    public void initialize() {
        client.start();

        try {
            client.create().forPath(path, Message.getDefault());
        } catch (KeeperException.NodeExistsException e) {
            //  ignore
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.out.println("Initialization failed");
            System.exit(1);
        }
    }

    @Override
    public void terminate() {
        try {
            client.delete().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            //  ignore
        } catch (Exception e) {
            //  ignore
        } finally {
            client.close();
        }
    }
}
