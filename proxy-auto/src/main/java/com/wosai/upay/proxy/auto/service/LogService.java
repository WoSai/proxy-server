package com.wosai.upay.proxy.auto.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wosai.upay.proxy.auto.exception.ParameterValidationException;
import com.wosai.upay.proxy.util.StringUtil;
import com.wosai.upay.proxy.util.ZipUtil;

/**
 * 日志工具
 * @author qi
 *
 */
public class LogService {
	
	public static ThreadLocal<String> terminalSN=new ThreadLocal<String>();

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    
    private static final Map<String,String> dateMap=new HashMap<String,String>();
    
    private static final Map<String,BufferedWriter> writerMap=new HashMap<String,BufferedWriter>();
    
    private String logDir="D:/log/upay/";
    
    private String suffix = ".log";
	
    /**
     * 记录请求日志
     * @param request
     * @param method
     * @param level
     * @param status
     * @param msg
     * @param latency
     */
	public void logRequest(HttpServletRequest request,String method,String level,String status,String msg,long latency){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		sb.append("\"_index\":\"").append(getRequestId(request)).append("\",");
		sb.append("\"latency\":\"").append(latency).append("\",");
		sb.append("\"latency_pretty\":\"").append(latency/1000000d).append("\",");
		sb.append("\"method\":\"").append(method).append("\",");
		sb.append("\"level\":\"").append(level).append("\",");
		sb.append("\"msg\":\"").append(msg).append("\",");
		sb.append("\"path\":\"").append(request.getPathInfo()).append("\",");
		sb.append("\"remote\":\"").append(request.getRemoteAddr()).append("\",");
		sb.append("\"size\":\"").append(request.getContentLength()).append("\",");
		sb.append("\"time\":\"").append(Calendar.getInstance().getTime()).append("\"");
		sb.append("}\n");
		
		logger.info(sb.toString());
		try {
			BufferedWriter writer = this.getWriter();
			if(writer!=null){
				//避免日志错乱
				synchronized(writer){
					writer.write(sb.toString());
					writer.newLine();
					writer.flush();
					logger.debug(" request log  is completed ");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录请求日志
	 * @param request
	 * @param latency
	 */
	public void logRequest(HttpServletRequest request,String level, long latency){
		this.logRequest(request,"post",level,"200","handle request",latency);
	}
	
	/**
	 * 计算request唯一标识
	 * @param request
	 * @return
	 */
	private String getRequestId(HttpServletRequest request){
		return this.resolveRequestId(request);
	}
	
	/**
	 * 计算request唯一标识
	 * @param request
	 * @return
	 */
	private String resolveRequestId(HttpServletRequest request){
		//暂时用sessionid作为请求唯一标识
		return request.getRequestedSessionId();
	}

	/**
	 * 把设备编号保存到线程中
	 * @param terminalSn
	 */
	public void setTerminalSn(String terminalSn){
		terminalSN.set(terminalSn);
	}
	
	/**
	 * 从线程中获取设备编号
	 * @return
	 */
	private String getTerminalSn(){
		return terminalSN.get();
	}
	
	/**
	 * 获取输出文件流
	 * @return
	 * @throws IOException
	 */
	private BufferedWriter getWriter() throws IOException{
		logger.debug(" logging request ");
		String terminalSn = this.getTerminalSn();
		if(StringUtil.empty(terminalSn)){
			logger.debug(" terminalSn not found ");
			return null;
		}
		String now = new SimpleDateFormat("yyyy-mm-dd").format(Calendar.getInstance().getTime());
		String date = dateMap.get(terminalSn);
		BufferedWriter bw = writerMap.get(terminalSn);
		if(bw==null || date==null || !date.equals(now)){
			if(bw!=null){
				bw.close();
			}
			File file=new File(new StringBuilder(logDir).append(terminalSn).append(suffix).toString());
			file.createNewFile();
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));
			dateMap.put(terminalSn, now);
			writerMap.put(terminalSn, bw);
		}
		return bw;
	}
	
	/**
	 * 关闭日志文件流并删除文件
	 * @param terminalSn
	 */
	public void remove(String terminalSn) {
		BufferedWriter bw = writerMap.get(terminalSn);
		synchronized(writerMap){
			synchronized(bw){
				dateMap.remove(terminalSn);
				writerMap.remove(terminalSn);
				try {
					bw.close();
					new File(new StringBuilder(logDir).append(terminalSn).append(suffix).toString()).delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 获取待上传日志的集合
	 * @return
	 */
	public Map<String,String> list(){
		Map<String,String> map=new HashMap<String,String>();
		
		File file=new File(logDir);
		if(!file.exists()){
			throw new ParameterValidationException(new StringBuilder(logDir).append(" not exists. ").toString());
		}
		if(!file.isDirectory()){
			throw new ParameterValidationException(new StringBuilder(logDir).append(" not directory. ").toString());
		}
		//遍历需要上传的日志文件夹下的日志文件
		File[] logs=file.listFiles();

		for(File log:logs) {
			String terminalSn=log.getName().split("\\.")[0];
			if(!StringUtil.empty(terminalSn)&&!terminalSn.equals("null")){
				//获取文件的压缩内容
				try {
					logger.debug(new StringBuilder(terminalSn).append(" is compressing.").toString());
					map.put(terminalSn, ZipUtil.zipByFile(log));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}

	public String getLogDir() {
		return logDir;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
