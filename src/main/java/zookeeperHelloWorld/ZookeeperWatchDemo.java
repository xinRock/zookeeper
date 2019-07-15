package zookeeperHelloWorld;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZookeeperWatchDemo {
	
	private ZooKeeper zookeeper;
    private String oldValue = "";
    private String newValue = "";
    
    
    public ZookeeperWatchDemo() {
		super();
	}


	public ZooKeeper zkConnect() throws IOException {
        String path = "127.0.0.1:2181";
        zookeeper = new ZooKeeper(path, 20 * 1000, null);
        return zookeeper;
    }

    
    public void createZnode(String path, byte[] value, Watcher watcher, CreateMode node ) throws KeeperException, InterruptedException {
        zookeeper.create(path, value, ZooDefs.Ids.OPEN_ACL_UNSAFE, node);
    }

    public String getZnodeValue(final String path ) throws KeeperException, InterruptedException {
        byte[] data = zookeeper.getData(path, new Watcher() {

			public void process(WatchedEvent arg0) {
				//这里只走一个，watch类似个另起的线程监听
				System.out.println("观察着的过程");
				triggerWatch(path);				
			}
            
        }, new Stat());
        oldValue = new String(data);
        return new String(data);
    }

    public boolean triggerWatch (final String path) {
        byte[] data = new byte[0];
        try {
            data = zookeeper.getData(path, new Watcher() {
                
                public void process(WatchedEvent arg0) {
                	//回调自己，点睛之笔
                    triggerWatch(path);
                }
            }, new Stat());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newValue = new String(data);
        if (oldValue.equals(newValue)) {
            System.out.println("on change");
            return false;
        } else {
            System.out.println("oldvalue: " + oldValue + "new value: "  + newValue);
            oldValue = newValue;
            return true;
        }
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        //创建
        ZookeeperWatchDemo zookeeperWatchDemo = new ZookeeperWatchDemo();
        ZooKeeper zooKeeper = zookeeperWatchDemo.zkConnect();
        String path = "/amberas";
        String value = "hahahahaha";
        if (zooKeeper.exists(path, false) == null) {
            zookeeperWatchDemo.createZnode(path, value.getBytes(), null, CreateMode.PERSISTENT);
        }

        String znodeValue = zookeeperWatchDemo.getZnodeValue(path);
        System.out.println(znodeValue);

        Thread.sleep(1000 * 60 * 50);
    }
}
