
package config;

import io.netty.util.concurrent.FastThreadLocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RunningConfig {
	private static FastThreadLocal<String> serial=new FastThreadLocal<String>();
	
	private static FastThreadLocal<String> agent_num=new FastThreadLocal<String>();

	/** 接口配置Map <Key, Value> key为机构编号 value为配置项 */
	public static ConcurrentHashMap<String, Map<String,String>> configMap = new ConcurrentHashMap<>();


	public static String getSerial() {
		return serial.get();
	}

	public static void setSerial(String serial) {
		RunningConfig.serial.set( serial);
	}

	public static String getAgent_num() {
	
		return agent_num.get();
	}

	public static void setAgent_num(String agent_num) {
	
		RunningConfig.agent_num.set(agent_num);
	}
	
	public static void clear(){
		FastThreadLocal.removeAll();
	}
	
}

	