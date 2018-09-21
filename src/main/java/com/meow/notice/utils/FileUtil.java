package com.meow.notice.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileUtil {
	private static final String FILE_PATH = FileUtil.class.getClassLoader().getResource("").getPath();

	public static boolean saveToFile(String noticeInfo) {
		FileOutputStream outputStream = null;
		try {
			File noticeFile = getFile();
			outputStream = new FileOutputStream(noticeFile);
			outputStream.write(noticeInfo.getBytes());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private static File getFile() throws IOException {
		File noticeFolder = new File(FILE_PATH);
		if (!noticeFolder.exists()) {
			noticeFolder.mkdirs();
		}
		File noticeFile = new File(FILE_PATH + "/noticeFile");
		if (!noticeFile.exists()) {
			noticeFile.createNewFile();
		}
		return noticeFile;
	}

	public static String readFromFile() {
		Reader reader = null;
		try {
			File file = getFile();
			String builder = "";
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				if ((char) tempchar != '\r') {
					builder = builder + (char) tempchar;
				}
			}
			return builder;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
