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

	/**
	 * 获取本地映射的client_sn
	 * @param client_sn
	 * @return
	 */
	public String getMappingClientSn(String client_sn){
		if(client_sn==null){
			throw new ParameterValidationException("client_sn invalid");
		}
		String num = StringUtil.getCodeByNum(clientSnTimerStore.getCurrentTimerNum(client_sn), CODE_LENGTH);
		return new StringBuilder(client_sn).append(CODE_SPLIT_CHAR).append(num).toString();
	}
	
	/**
	 * 生成下个版本的client_sn
	 * @param client_sn
	 * @return
	 */
	public String generateMappingClientSn(String client_sn){
		if(client_sn==null){
			throw new ParameterValidationException("client_sn invalid");
		}
		Long num = clientSnTimerStore.getFixedTimerNum(client_sn);
		//如果没有成功的序号，则生成下一个
		if(num == 0l){
			num = clientSnTimerGenerate.generateNextTimerNum(client_sn);
		}
		String numStr = StringUtil.getCodeByNum(num, CODE_LENGTH);
		return new StringBuilder(client_sn).append(CODE_SPLIT_CHAR).append(numStr).toString();
	}
	
	/**
	 * 成功后固化client_sn
	 * @param client_sn
	 */
	public void fixedMappingClientSn(String client_sn){
		clientSnTimerGenerate.fixedNextTimerNum(client_sn);
	}

	public void setClientSnTimerStore(ClientSnTimerStore clientSnTimerStore) {
		this.clientSnTimerStore = clientSnTimerStore;
	}

	public void setClientSnTimerGenerate(ClientSnTimerGenerate clientSnTimerGenerate) {
		this.clientSnTimerGenerate = clientSnTimerGenerate;
	}

}
