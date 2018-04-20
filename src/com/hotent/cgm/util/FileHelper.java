
package com.hotent.cgm.util;

import java.io.*;
import java.util.List;

public class FileHelper {
	
	public static String readFile(String fileName, String charset) {
		try {
			File file = new File(fileName);
			StringBuffer sb = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset));
			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str + "\r\n");
			}
			in.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getCharset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					// 鍗曠嫭鍑虹幇BF浠ヤ笅鐨勶紝涔熺畻鏄疓BK
					if (0x80 <= read && read <= 0xBF)
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF)// 鍙屽瓧鑺� (0xC0 - 0xDF)
							// (0x80 -
							// 0xBF),涔熷彲鑳藉湪GB缂栫爜鍐�
							continue;
						else break;
						// 涔熸湁鍙兘鍑洪敊锛屼絾鏄嚑鐜囪緝灏�
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else break;
						} else break;
					}
				}
				
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}
	
	/**
	 * 澶囦唤鏂囦欢銆� 濡傛灉鏈夊浠芥枃浠讹紝鍏堝垹闄ゃ��
	 * 
	 * @param filePath
	 */
	public static void backupFile(String filePath) {
		
		File file = new File(filePath);
		if (file.exists()) {
			String path = filePath + ".001";
			File tmp = new File(path);
			int i = 1;
			
			while (tmp.exists()) {
				i++;
				int len = 3 - String.valueOf(i).length();
				String ext = String.valueOf(i);
				for (int k = 0; k < len; k++) {
					ext = "0" + ext;
				}
				path = filePath + "." + ext;
				tmp = new File(path);
			}
			copyFile(filePath, path);
		}
		
	}
	
	/**
	 * 鍐欏叆鏂囦欢
	 * 
	 * @param fileName
	 * @param charset
	 * @param content
	 * @throws IOException
	 */
	public static void writeFile(File file, String charset, String content) throws IOException {
		Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset);
		writer.write(content);
		writer.close();
	}
	
	public static void main(String[] args) {
		String insert = "<!--insert-->";
		String str = "aa<!--insert-->bb<!--insert-->cc";
		int pos = str.lastIndexOf(insert);
		System.out.println(str.substring(0, pos));
		System.out.println(str.substring(pos + insert.length()));
	}
	
	public static String[] getBySplit(String content, String splitTag) {
		String[] aryRtn = new String[2];
		int pos = content.lastIndexOf(splitTag);
		aryRtn[0] = content.substring(0, pos);
		aryRtn[1] = content.substring(pos + splitTag.length());
		return aryRtn;
	}
	
	public static boolean copyFile(String from, String to) {
		File fromFile = new File(from);
		File toFile = new File(to);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try {
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);
			int bytesRead;
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			while ((bytesRead = fis.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
			}
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			System.out.println(e);
			return false;
		}
		return true;
		
	}
	
	public static boolean isExistFile(String dir) {
		boolean isExist = false;
		File fileDir = new File(dir);
		if (fileDir.isDirectory()) {
			File[] files = fileDir.listFiles();
			if (files != null && files.length != 0) {
				isExist = true;
			}
		}
		return isExist;
	}
	
	/**
	 * 鏍规嵁鐩綍鍙栧緱鏂囦欢鍒楄〃
	 * 
	 * @param path
	 * @return
	 */
	public static File[] getFiles(String path) {
		File file = new File(path);
		return file.listFiles();
	}
	
	public static InputStream getInputStream(String filepath) {
		File file = new File(filepath);
		String charset = getCharset(file);
		String str = readFile(filepath, charset);
		ByteArrayInputStream stream = null;
		try {
			stream = new ByteArrayInputStream(str.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return stream;
	}
	
}
