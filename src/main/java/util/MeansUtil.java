package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("all")
public class MeansUtil{
	
	public final static String DEFAULT_ENCODE = "UTF-8";
	private final String HEX_STRING = "0123456789ABCDEF";
	private final String defultCode = "UTF-8";

	public boolean checkByte(byte[] byteData) throws Exception {
		return ("0000".equals(byte2HexStr(byteData))) ? true : false;
	}
	
	public String asciiToString(byte[] value) {
		try {
			return new String(value, DEFAULT_ENCODE);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public String getRequestParamString(Map<String, String> requestParam, String coder) {
		if (null == coder || "".equals(coder)) {
			coder = "UTF-8";
		}
		StringBuffer sf = new StringBuffer("");
		String reqstr = "";
		if (null != requestParam && 0 != requestParam.size()) {
			for (Entry<String, String> en : requestParam.entrySet()) {
				try {
					sf.append(en.getKey()
							+ "="
							+ (null == en.getValue() || "".equals(en.getValue()) ? "" : URLEncoder
									.encode(en.getValue(), coder)) + "&");
				} catch (UnsupportedEncodingException e) {
					return "";
				}
			}
			reqstr = sf.substring(0, sf.length() - 1);
		}
		return reqstr;
	}
	
	public byte[] mapToByte(Map<String, String> dataMap) throws Exception {
		String str = JSONObject.toJSONString(dataMap);
		byte[] by = str.getBytes(defultCode);
		String len = "0000" + by.length;
		len = len.substring(len.length() - 4);
		byte[] lenlen = len.getBytes(defultCode);
		byte[] pack = new byte[4 + by.length];
		System.arraycopy(lenlen, 0, pack, 0, 4);
		System.arraycopy(by, 0, pack, 4, by.length);
		return pack;
	}
	
	public BigDecimal convertMoney(String money) throws Exception {
		String moneyStr1 = money.substring(0, money.length() - 2);
		String moneyStr2 = money.substring(money.length() - 2);
		String moneyStr = moneyStr1 + "." + moneyStr2;
		return new BigDecimal(moneyStr).setScale(2, BigDecimal.ROUND_DOWN);
	}
	
	public String changeSize(int size) {
		String s = "00" + size;
		return s.substring(s.length() - 2);
	}
	
	public String getObject(Map<String, String> map, String key) {
		return map.containsKey(key) ? ((Object) map.get(key)).toString() : "";
	}
	
	public byte[] assemble(byte[]... b) throws Exception {
		int length = 0;
		for (byte[] bl : b) {
			if (bl != null){
				length += bl.length;
			}
		}
		byte[] data = new byte[length];
		int count = 0;
		for (int i = 0; i < b.length; i++) {
			if (b[i] != null) {
				System.arraycopy(b[i], 0, data, count, b[i].length);
				count += b[i].length;
			}
		}
		return data;
	}

	public boolean checkArrays(String[] array, String str)
			throws Exception {
		for (String mt : array) {
			if (mt.equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public byte[] hexStr2Bytes(String src) throws Exception {
		int m = 0, n = 0;
		int l = src.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
		}
		return ret;
	}

	private byte uniteBytes(String src0, String src1) throws Exception {
		byte b0 = Byte.decode( "0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode( "0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);
		return ret;
	}

	public String bytesToString(byte[] bytes, int off, int len, int maxLen,
			String enc) {
		String ret = null;
		if (enc == null){
			enc = "UTF-8";
		}
		if (bytes != null) {
			if (len > maxLen){
				len = maxLen;
			}
			try {
				ret = new String(bytes, off, len, enc);
			} catch (Exception e) {
			}
			if (ret == null) { // 否则使用缺省编码方式
				try {
					ret = new String(bytes, off, len);
				} catch (Exception e) {
				}
			}
		}
		return ret;
	}

	public String byte2HexStr(byte[] b) throws Exception {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			hs = hs+(stmp.length() == 1 ? "0"+stmp : stmp);
		}
		return hs.toUpperCase();
	}

	public int byteArrayToShort(byte[] b) throws Exception {
		return (b[0] << 8) + (b[1] & 0xFF);
	}

	public String initSize(String i) throws Exception {
		String j = "0000" + i;
		return j.substring(j.length() - 4, j.length());
	}

	public Map<String, String> turnAnd(String data) {
		String[] jx = data.split("\\&");
		Map<String, String> map = new HashMap<String, String>();
		for (String string : jx) {
			int n = string.indexOf("=");
			String value = string.substring(n + 1);
			String key = string.substring(0, n);
			map.put(key, value);
		}
		return map;
	}

	public Map<String, String> changeJsonToMap(String jsonStr) throws Exception {
		return (Map<String, String>)JSON.parse(jsonStr);
	}

	public Map<String, Object> changeJsonToMaps(String jsonStr) throws Exception {
		return (Map<String, Object>)JSON.parse(jsonStr);
	}


	public String changeMapToJson(Map<String, String> map) throws Exception {
		return JSONObject.toJSONString(map);
	}




}
