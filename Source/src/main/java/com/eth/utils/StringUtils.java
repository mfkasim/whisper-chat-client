package com.eth.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class StringUtils {

	public static String fromHex(String strAsHex) {

		String normalString = null;
		// remove 0x
		strAsHex = strAsHex.substring(2);
		byte[] bytes = new byte[strAsHex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = Byte.parseByte(strAsHex.substring(2 * i, 2 * i + 2), 16);

		}
		try {
			normalString = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return normalString;
	}

	public static String toHex(String str) {
		StringBuilder builder = new StringBuilder();
		builder.append("0x");
		try {
			byte[] bytesUTF8 = str.getBytes("UTF-8");

			// System.out.println(Arrays.toString(bytesUTF8));

			for (int i = 0; i < bytesUTF8.length; i++) {
				int intNum = bytesUTF8[i];
				builder.append(Integer.toString(intNum, 16));
				// System.out.println(builder.toString());

			}
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		return builder.toString();

	}

	public static String getTopicAsHex(String topic) {
		String topicAsHex = toHex(topic);
		if (topicAsHex.length() > 10)
			topicAsHex = topicAsHex.substring(0, 10);
		for (int i = topicAsHex.length(); i < 10; i++) {
			topicAsHex += "0";
		}
		
//		System.out.println("topicAsHex="+ topicAsHex);
		
		return topicAsHex;
	}

//	public static void main(String[] args) {
//		String normal = fromHex("0x48454c4c4f");
//
//		String hex = toHex("HELLO");
//		System.out.println("hex=" + hex + ", normal=" + normal);
//	}
}
