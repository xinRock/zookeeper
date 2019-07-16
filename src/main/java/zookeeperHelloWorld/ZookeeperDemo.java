package zookeeperHelloWorld;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import zookeeperHelloWorld.util.ZKclient;

public class ZookeeperDemo {

	private String oldValue = "";
	private String newValue = "";
	private ZooKeeper zkConnect = null;
	

	public ZookeeperDemo() {
		super();
		this.zkConnect = ZKclient.zkConnect();
	}
	@Test
	public void setNodeValue(){
		String path="/asdfdsa";
		try {
			ZKclient.zkConnect().setData(path, "newda2".getBytes(), -1);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 // 删除没有子节点的节点 对应delete path [version] 当version=-1时表示忽略版本号比对
	@Test
	public void deleteZnode() throws Exception {

		String path = "/asdfdsa";
		String version = "-1";
		if (version == null) {
			ZKclient.zkConnect().delete(path, -1);
		} else {
			ZKclient.zkConnect().delete(path, Integer.parseInt(version));
		}
	}

	@Test
	public void createZnode() {
		try {
			ZKclient.zkConnect().create("/asdfdsa", "sdsd".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("dfdfsa");
	}

	// 观察者Demo
	public String getZnodeValue(final String path) throws KeeperException, InterruptedException {
		byte[] data = this.zkConnect.getData(path, new Watcher() {

			public void process(WatchedEvent arg0) {
				// 这里只走一个，watch类似个另起的线程监听
				System.out.println("观察着的过程");
				triggerWatch(path);
			}

		}, new Stat());
		oldValue = new String(data);
		return oldValue;
	}

	public boolean triggerWatch(final String path) {
		byte[] data = new byte[0];
		try {
			data = this.zkConnect.getData(path, new Watcher() {
				public void process(WatchedEvent arg0) {
					// 回调自己，点睛之笔
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
			System.out.println("oldvalue: " + oldValue + "new value: " + newValue);
			oldValue = newValue;
			return true;
		}
	}

	/*
	 * 观察者案例，观察对象存活时间是根据主线程的运行时间，如果主线程运行完了，那观察者对象失效
	 */
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		// 创建
		ZookeeperDemo zookeeperWatchDemo = new ZookeeperDemo();
		String path = "/asdfdsa";
		String znodeValue = zookeeperWatchDemo.getZnodeValue(path);
		System.out.println(znodeValue);
		System.out.println("成功了");
		TimeUnit.MINUTES.sleep(5);
	}
}
