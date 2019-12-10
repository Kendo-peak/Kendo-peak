package socket.nettyServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ExportService;
import service.impl.ExportServiceImpl;
import socket.BaseIO;
import util.LogFormat;
import util.MeansUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("all")
public class Server extends BaseIO {
	private static Logger log= LoggerFactory.getLogger(Server.class);
	private ServerSocket serverSocket;
	private MeansUtil loFunction = new MeansUtil();


	public Server() throws IOException {
		serverSocket = new ServerSocket(19013);
		System.out.println("Server Start..."+serverSocket.getLocalPort());
	}

	public void service() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(150); // 创建工作线程的线程池
		while (true) {
			Socket socket;
			try {
				socket = serverSocket.accept();
				executorService.execute(createTask(socket)); // 分配一个线程
			} catch (IOException e) {
				log.error(LogFormat.ErrLogFormat("thread Exception", null, e));
			}
		}
	}
	private Runnable createTask(final Socket socket) {
		return new Runnable() {
			String pack = "";
			InputStream input = null;
			OutputStream output = null;
			@Override
			public void run() {
				try {
					input = socket.getInputStream();
					output = socket.getOutputStream();
					socket.setSoTimeout(30000);
//					byte[] lenByte = new byte[2];
//					input.read(lenByte, 0, 2);
//					int len = (int) loFunction.byteArrayToShort(lenByte);
					int len = 0;
					while (len == 0) {
						len = input.available();
					}
					byte[] bb = new byte[len];
					int rdLen = input.read(bb, 0, 2);
					while(rdLen < len){//如果数据不等于实际长度则继续读取
						rdLen += input.read(bb, rdLen, input.available());
					}
					pack = loFunction.byte2HexStr(bb);
					pack = loFunction.bytesToString(bb, 0, len,len, "UTF-8");
					//json转成list
					List<LinkedHashMap<String,String>> reqList = JSON.parseObject(pack,new TypeReference<List<LinkedHashMap<String,String>>>(){});
					//生成excel并上传至文件服务器
					ExportService es=new ExportServiceImpl();
					es.increaseExcel(reqList);
				} catch (Exception e) {
					log.error(LogFormat.ErrLogFormat("Reveice Data Exception" + pack, null, e));
				} finally {
					closeIO(socket, input, output);
				}
			}

		};
	}
}
