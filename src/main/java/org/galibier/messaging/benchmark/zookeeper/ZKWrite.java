package org.galibier.messaging.benchmark.zookeeper;

import org.apache.zookeeper.KeeperException;

public class ZKWrite extends ZKOperation {
    public ZKWrite(String host, String path) {
        super(host, path);
    }

    @Override
    public boolean execute() {
        try {
            client.setData().forPath(path);
            return true;
        } catch (KeeperException.NoNodeException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
