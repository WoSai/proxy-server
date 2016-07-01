package com.wosai.upay.proxy.upay.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ClientSnTimerStore {
	
	/**
	 * 获取当前client_sn在计时器中的序号
	 * @param client_sn
	 * @return
	 */
	@Cacheable(value="order_client_sn",key="#client_sn")
	public Long getCurrentTimerNum(String client_sn){
		return 0l;
	}
	
	
}
