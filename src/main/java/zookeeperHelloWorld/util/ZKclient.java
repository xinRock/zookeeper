package zookeeperHelloWorld.util;

import java.io.IOException;

import org.apache.zookeeper.ZooKeeper;


public class ZKclient {

//	private static  ZooKeeper zkConnect;
//	
//	public ZKclient() {
//		super();
//		this.zkConnect = zkConnect();
//	}

	public static  ZooKeeper zkConnect() {
		String path = "127.0.0.1:2181";
		ZooKeeper zooKeeper = null;
		try {
			 zooKeeper = new ZooKeeper(path, 20 * 10000, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return zooKeeper;
	}

}
