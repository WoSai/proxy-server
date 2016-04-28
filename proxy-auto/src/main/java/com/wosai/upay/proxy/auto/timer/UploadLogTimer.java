package com.wosai.upay.proxy.auto.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wosai.upay.proxy.auto.service.ProxyAutoService;

/**
 * 定期上传日志
 * @author qi
 *
 */
public class UploadLogTimer extends BaseTimerTask implements IBaseTimerTask {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadLogTimer.class);
	
	@Autowired
	private ProxyAutoService proxyAutoService;

	@Override
	public void run() {
		logger.debug(new StringBuilder("timer running. '").toString());
		
		proxyAutoService.uploadLog();
		
		logger.debug(new StringBuilder("timer running end. ").toString());
	}

	public void setProxyAutoService(ProxyAutoService proxyAutoService) {
		this.proxyAutoService = proxyAutoService;
	}

}
