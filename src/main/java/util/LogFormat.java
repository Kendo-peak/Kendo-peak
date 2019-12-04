
package util;


import config.RunningConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class LogFormat {
	private static String br="\r\n";
	public static String ErrLogFormat(String msg,byte[] pack, Exception e) {
		StringBuffer logMsgBf = new StringBuffer();
		logMsgBf.append("[Serial]:[" + RunningConfig.getSerial() + "]" + br);
		logMsgBf.append("[Msg]:[" + msg + "]" + br);
		if(pack!=null)
		logMsgBf.append("[Pack]:["+LoUtils.byte2HexStr(pack)+"]"+br);
		if (e != null)
			logMsgBf.append("[Exp]:[" + br + changeExceptionInfo(e) + "]" + br
					+ br);
		return logMsgBf.toString();
	}
	public static String ErrLogFormat(String msg,String address,int access,byte[] pack, Exception e) {
		StringBuffer logMsgBf = new StringBuffer();
		if(access!=99)
		logMsgBf.append("[Serial]:[" + RunningConfig.getSerial() + "]" + br);
		logMsgBf.append("[Msg]:[" + msg + "]" + br);
		logMsgBf.append("[ReqAdd]:["+address+"]"+br);
		if(pack!=null)
		logMsgBf.append("[Pack]:["+LoUtils.byte2HexStr(pack)+"]"+br);
		if (e != null)
			logMsgBf.append("[Exp]:[" + br + changeExceptionInfo(e) + "]" + br
					+ br);
		return logMsgBf.toString();
	}
	public static String infoFormat(String type,String msg) {
		StringBuffer logMsgBf = new StringBuffer();
		logMsgBf.append("[Serial]:[" + RunningConfig.getSerial() + "]" + br);
		logMsgBf.append("[type]:[" + type + "]" + br);
		logMsgBf.append("[Msg]:[" + msg + "]" + br);
		return logMsgBf.toString();
	}
	public static String ckLog(String msg,int access){
		StringBuffer sb=new StringBuffer();
		sb.append("[Msg]:["+msg+"]"+br);
		return sb.toString();
	}
	public static String changeExceptionInfo(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	public static final List<String> noCon=new ArrayList<String>(){
		private static final long serialVersionUID = 1L;

	{
		add("2");
		add("35");
		add("36");
		add("53");
		add("55");
		add("56");
	}};
}

	