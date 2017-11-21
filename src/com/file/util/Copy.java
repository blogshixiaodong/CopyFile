package com.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class Copy {
	public static String SRC_PATH = "C:\\Users\\Administrator\\Desktop\\大三上\\Java Web\\JSP\\MyEclipse\\studentManager";
	public static String DEST_PATH = "F:\\backups";
	public static int backupFileCount = 0;
	public static long backupTime = 0;
	
	public static void main(String[] args) throws IOException {
		
//		if(!checkArgs(args))	return ;
		setDestFolder();
		start();
		copyFolder(SRC_PATH, DEST_PATH);
		end();
		System.out.println("备份文件数：" + backupFileCount);
		System.out.println("备份时间：" + backupTime + "毫秒");
		shutdown();
	}
	
	private static void shutdown() throws IOException {
		//Runtime.getRuntime().exec("cmd /c Shutdown -t 10");
	}

	private static boolean checkArgs(String[] args) {
		if(args!= null && 2 == args.length) {
			SRC_PATH = args[0];
			DEST_PATH = args[1];
			return true;
		} else {
			System.out.println("example: java Copy (1) (2)");
			System.out.println("1. src path");
			System.out.println("2. dest path");
			return false;
		}
		
	}

	private static void setDestFolder() {
		String folderName = SRC_PATH.substring(SRC_PATH.lastIndexOf("\\"));
		DEST_PATH += File.separator + folderName + " " + TimeUtil.getYMD("_");
		
	}

	public static void start() {
		backupTime = System.currentTimeMillis();
	}
	public static void end() {
		backupTime = System.currentTimeMillis() - backupTime;
	}
	
	public static boolean copyFolder(String srcPath, String destPath) throws IOException {
		File srcFolder = new File(srcPath);
		File destFolder = new File(destPath);
		if(!destFolder.exists()) {
			destFolder.mkdirs();
		}
		File[] fileList = srcFolder.listFiles();
		for(File f : fileList) {
			if(f.isFile()) {
				backupFileCount++;
				copyFile(srcPath + File.separator + f.getName(), destPath);
			} else if(f.isDirectory()) {
				copyFolder(f.getPath(), destPath + File.separator + f.getName());
			}
			
		}
		return true;
	}
	
	//copy single file
	public static boolean copyFile(String srcPath, String destPath) throws IOException {
		File srcFile = new File(srcPath);
		if(!srcFile.exists() || srcFile.isDirectory()) {
			System.out.println("source file path error, please check again!");
			return false;
		}
	
		int byteSum = 0;
		int byteCount = 0;
		byte[] buffer = new byte[1024];
		
		if(srcFile.exists()) {
			FileInputStream inStream = new FileInputStream(srcFile);
			FileOutputStream fos = new FileOutputStream(destPath + File.separator + srcFile.getName());
			while(-1 != (byteCount = inStream.read(buffer))) {
				byteSum += byteCount;
				fos.write(buffer);
			}
			inStream.close();
			fos.close();
			System.out.println("Copy " + srcFile.getName() + " Success(total:" + byteSum + "bytes).");
			return true;
		}
		return false;
	}
	
	
	public static boolean fileChannelCopy(File srcFile, File destFile) throws IOException {
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		FileChannel in = fis.getChannel();
		FileChannel out= fos.getChannel();
		in.transferTo(0, in.size(), out);
		return true;
	}
	
}
