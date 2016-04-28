package com.wosai.upay.proxy.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具
 * @author qi
 *
 */
public class ZipUtil {
	
	private static final int BUFFER = 4*1024;
	private static final String encoding = "UTF-8";
	
	/**
	* 使用zip进行压缩
	* @param str 压缩前的文本
	* @return 返回压缩后的文本
	*/
	public static final String zip(String str) {
		if (str == null){
			return null;
		}
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = new sun.misc.BASE64Encoder().encodeBuffer(compressed);
		} catch (IOException e) {
			compressed = null;
		} finally {
			if (zout != null) {
				try {
					zout.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return compressedStr;
	}
	
	/**
	* 读取文件内容并使用zip进行压缩后返回
	* @param str 压缩前的文本
	* @return 返回压缩后的文本
	 * @throws IOException 
	*/
	public static final String zipByFile(File file) throws IOException {
		StringBuilder sb=new StringBuilder();
		//读文件内容
		InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		while((lineTxt = bufferedReader.readLine()) != null){
			sb.append(lineTxt);
		}
		read.close();

		return zip(sb.toString());
	}
	
	/**
	 * 压缩制定路径生成对应压缩包文件
	 * @param srcPath
	 * @param destPath
	 * @throws IOException
	 */
	public static void zip(String srcPath,String destPath) throws IOException{
		File file = new File(srcPath);
		File destFile = new File(destPath);
        if (!file.exists()){
            throw new RuntimeException(srcPath + " not exist！");    
        }
        if (!destFile.exists()){
        	destFile.createNewFile();
        }
        if (file.isDirectory()){  
        	ZipUtil.zipDir(file, destFile);   
        } else if(file.isFile()){
        	ZipUtil.zipFile(file, destFile);   
        }
	}

	/**
	 * 压缩目录生成对应文件
	 * @param file
	 * @param destFile
	 * @throws IOException
	 */
	private static void zipDir(File file,File destFile) throws IOException{
        FileOutputStream fileOutputStream = new FileOutputStream(destFile);    
        CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());    
        ZipOutputStream out = new ZipOutputStream(cos);
        File[] files=file.listFiles();
        for(File child:files){
        	zipFile(child, out, new StringBuilder(file.getName()).append("/").toString());
        }
        out.close();
	}
	
	/**
	 * 压缩指定文件生成对应压缩文件
	 * @param file
	 * @param destFile
	 * @throws IOException
	 */
	private static void zipFile(File file,File destFile) throws IOException{
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        FileOutputStream fileOutputStream = new FileOutputStream(destFile);    
        CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());    
        ZipOutputStream out = new ZipOutputStream(cos);   
        ZipEntry entry = new ZipEntry(file.getName()); 
        out.putNextEntry(entry);    
        
        int count;    
        byte data[] = new byte[BUFFER];    
        while ((count = bis.read(data, 0, BUFFER)) != -1) {    
            out.write(data, 0, count);    
        }    
        bis.close();
        out.close();
	}
	
	/**
	 * 目录递归压缩
	 * @param file
	 * @param out
	 * @param parentDir
	 * @throws IOException
	 */
	private static void zipFile(File file,ZipOutputStream out,String parentDir) throws IOException{
		if(file.isDirectory()){
			File[] files=file.listFiles();
			for(File child:files){
	        	zipFile(child, out, new StringBuilder(parentDir).append(file.getName()).append("/").toString());
	        }
		}else if(file.isFile()){
	        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
	        ZipEntry entry = new ZipEntry(file.getName()); 
	        out.putNextEntry(entry);    
	        
	        int count;    
	        byte data[] = new byte[BUFFER];    
	        while ((count = bis.read(data, 0, BUFFER)) != -1) {    
	            out.write(data, 0, count);    
	        }    
	        bis.close();
		}
	}
}
