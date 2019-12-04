package util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class LoUtils {
	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1){
				hs = hs + "0" + stmp;
			}else{
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	public static final int byteArrayToShort(byte[] b) {
		return (b[0] << 8) + (b[1] & 0xFF);
	}

	public static boolean checkByte(byte[] byteData) throws Exception {
		return ("0000".equals(byte2HexStr(byteData))) ? true : false;
	}

	public static byte[] hexStr2Bytes(String src) {
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

	private static byte uniteBytes(String src0, String src1) {
		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);
		return ret;
	}
	public static byte[] StrToBCDBytes(String s) {
		if (s.length() % 2 != 0) {
			s = "0" + s;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] cs = s.toCharArray();
		for (int i = 0; i < cs.length; i += 2) {
			int high = cs[i] - 48;
			int low = cs[i + 1] - 48;
			baos.write(high << 4 | low);
		}
		return baos.toByteArray();
	}
	public static byte[] StrToBCDBytesRight(String s) {
		if (s.length() % 2 != 0) {
			s = s + "0";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] cs = s.toCharArray();
		for (int i = 0; i < cs.length; i += 2) {
			int high = cs[i] - 48;
			int low = cs[i + 1] - 48;
			baos.write(high << 4 | low);
		}
		return baos.toByteArray();
	}

	public static byte[] getAsciiBytes(String input) {
		char[] c = input.toCharArray();
		byte[] b = new byte[c.length];
		for (int i = 0; i < c.length; i++){
			b[i] = (byte) (c[i] & 0x007F);
		}

		return b;
	}
	public static String bcdTostr(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int h = ((b[i] & 0xff) >> 4) + 48;
			sb.append((char) h);
			int l = (b[i] & 0x0f) + 48;
			sb.append((char) l);
		}
		return sb.toString();
	}
	public static boolean[] getBinaryFromByte(byte[] b) {
		boolean[] binary = new boolean[b.length * 8 + 1];
		String strsum = "";
		for (int i = 0; i < b.length; i++) {
			strsum += getEigthBitsStringFromByte(b[i]);
		}
		for (int i = 0; i < strsum.length(); i++) {
			if (strsum.substring(i, i + 1).equalsIgnoreCase("1")) {
				binary[i + 1] = true;
			} else {
				binary[i + 1] = false;
			}
		}
		return binary;
	}

	public static String getEigthBitsStringFromByte(int b) {
		b |= 256; // mark the 9th digit as 1 to make sure the string
		String str = Integer.toBinaryString(b);
		int len = str.length();
		return str.substring(len - 8, len);
	}
	public static int bcdToint(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int h = ((b[i] & 0xff) >> 4) + 48;
			sb.append((char) h);
			int l = (b[i] & 0x0f) + 48;
			sb.append((char) l);
		}
		return Integer.parseInt(sb.toString());
	}

	public static int numlen2bcdlen(int s) {
		int bcdlen = 0;
		if (s % 2 == 0) {
			bcdlen = s / 2;
		} else {
			bcdlen = (s + 1) / 2;
		}
		return bcdlen;

	}

	public static String asciiToString(byte[] value) {
		try {
			return new String(value, "GBK");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public static byte[] getByteFromBinary(boolean[] binary) {
		int num = (binary.length - 1) / 8;
		if ((binary.length - 1) % 8 != 0) {
			num = num + 1;
		}
		byte[] b = new byte[num];
		String s = "";
		for (int i = 1; i < binary.length; i++) {
			if (binary[i]) {
				s += "1";
			} else {
				s += "0";
			}
		}
		String tmpstr;
		int j = 0;
		for (int i = 0; i < s.length(); i = i + 8) {
			tmpstr = s.substring(i, i + 8);
			b[j] = getByteFromEigthBitsString(tmpstr);
			j = j + 1;
		}
		return b;
	}

	public static byte getByteFromEigthBitsString(String str) {
		byte b;
		if (str.substring(0, 1).equals("1")) {
			str = "0" + str.substring(1);
			b = Byte.valueOf(str, 2);
			b |= 128;
		} else {
			b = Byte.valueOf(str, 2);
		}
		return b;
	}
	public static String appendField(String one, String two, String third) {
		return new StringBuffer(one).append(two).append(third).toString();
	}
}
