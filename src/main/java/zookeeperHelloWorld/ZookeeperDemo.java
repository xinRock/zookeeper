package zookeeperHelloWorld;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * zookeeper
 * 连接zookeeper
 * 创建znode
 * 获取znode值
 * 断开链接
 */
public class ZookeeperDemo {
	
	private ZooKeeper zookeeper;
	
	public ZookeeperDemo() {
		super();
		try {
			zkConnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//连接zookeeper
	public ZooKeeper zkConnect() throws IOException{
		String basepath="127.0.0.1:2181";
		
		//第一个ip和端口，第二个参数是超时时间，第三个参数是观察者
		this.zookeeper = new ZooKeeper(basepath,20*1000, null);
		return zookeeper;
	}
	
	/**
     * 创建znode节点
     * @param path　znode的路径
     * @param value　znode的值
     * @param watcher
     * @param node //创建node的模式
     * @throws KeeperException
     * @throws InterruptedException
     */
	private void createZnode(String path,byte[] value,Watcher watch,CreateMode node) throws KeeperException, InterruptedException {
		zookeeper.create(path, value,ZooDefs.Ids.OPEN_ACL_UNSAFE, node);
	}
	
	/**
     * 通过path获得znode的值
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getZnodeValue(String path ) throws KeeperException, InterruptedException {
        //           第二个值是代表是否开启监听，这里还是先不管.第三个参数就是结构体
        byte[] data = zookeeper.getData(path, false, new Stat());
        return new String(data);
    }
    
    public void close() {
        try {
            if (zookeeper != null) {
                zookeeper.close();
            }
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
    }
	
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZookeeperDemo zookeeperDemo = new ZookeeperDemo();
        //获取连接
        //创建znode
        zookeeperDemo.createZnode("/amber2", "hahaha".getBytes(), null, CreateMode.PERSISTENT_SEQUENTIAL);
        //获取znode的值
        //String znodeValue = zookeeperDemo.getZnodeValue("/zktest/tem");
        //System.out.println(znodeValue);
        System.out.println("success");
        zookeeperDemo.close();

    }
}
