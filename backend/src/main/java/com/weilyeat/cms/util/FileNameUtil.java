package com.ttwijang.cms.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class FileNameUtil {
    public static String resolveFilename(final String userAgent, final String fileName) {
		String encodedFilename = null;
		try {
			if (userAgent.indexOf("MSIE") > -1) {
					encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			} else if (userAgent.indexOf("Trident") > -1) {
				encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			} else if (userAgent.indexOf("Chrome") > -1) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < fileName.length(); i++) {
					char c = fileName.charAt(i);
					if (c > '~') {
						sb.append(URLEncoder.encode("" + c, "UTF-8"));
					} else {
						sb.append(c);
					}
				}
				encodedFilename = sb.toString();
			} else if (userAgent.indexOf("Opera") > -1) {
				encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
			} else if (userAgent.indexOf("Safari") > -1) {
				encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
				encodedFilename = URLDecoder.decode(encodedFilename, "UTF-8");
			} else {
				encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
				encodedFilename = URLDecoder.decode(encodedFilename, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			encodedFilename = fileName;
		}
		return encodedFilename;
	}
}
