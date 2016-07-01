package com.wosai.upay.proxy.upay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.wosai.upay.proxy.upay.exception.ParameterValidationException;
import com.wosai.upay.proxy.util.StringUtil;

@Service
public class CachedSnMap {
	
	@Autowired
	private ClientSnTimerStore clientSnTimerStore;
	
	@Autowired
	private ClientSnTimerGenerate clientSnTimerGenerate;
	
	private static final int CODE_LENGTH = 4;
	
	private static final String CODE_SPLIT_CHAR = "_";

	public String getMappingClientSn(String client_sn){
		if(client_sn==null){
			throw new ParameterValidationException("client_sn invalid");
		}
		String num = StringUtil.getCodeByNum(clientSnTimerStore.getCurrentTimerNum(client_sn), CODE_LENGTH);
		return new StringBuilder(client_sn).append(CODE_SPLIT_CHAR).append(num).toString();
	}
	
	public String generateMappingClientSn(String client_sn){
		if(client_sn==null){
			throw new ParameterValidationException("client_sn invalid");
		}
		String num = StringUtil.getCodeByNum(clientSnTimerGenerate.generateNextTimerNum(client_sn), CODE_LENGTH);
		return new StringBuilder(client_sn).append(CODE_SPLIT_CHAR).append(num).toString();
	}

	public void setClientSnTimerStore(ClientSnTimerStore clientSnTimerStore) {
		this.clientSnTimerStore = clientSnTimerStore;
	}

	public void setClientSnTimerGenerate(ClientSnTimerGenerate clientSnTimerGenerate) {
		this.clientSnTimerGenerate = clientSnTimerGenerate;
	}

}
