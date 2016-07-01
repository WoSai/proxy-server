package com.wosai.upay.proxy.upay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

@Component
public class ClientSnTimerGenerate {
	
	@Autowired
	private ClientSnTimerStore clientSnTimerStore;

	/**
	 * 生成计时器中client_sn的下一个版本
	 * @param client_sn
	 * @return
	 */
	@CachePut(value="order_client_sn",key="#client_sn")
	public Long generateNextTimerNum(String client_sn){
		Long index = clientSnTimerStore.getCurrentTimerNum(client_sn);
		index++;
		return index;
	}

	public void setClientSnTimerStore(ClientSnTimerStore clientSnTimerStore) {
		this.clientSnTimerStore = clientSnTimerStore;
	}
	
	
}
