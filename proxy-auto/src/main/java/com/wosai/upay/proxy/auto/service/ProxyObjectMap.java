package com.wosai.upay.proxy.auto.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.wosai.data.dao.Criteria;
import com.wosai.data.dao.Dao;
import com.wosai.upay.proxy.auto.model.ClientMerchant;
import com.wosai.upay.proxy.auto.model.ClientStore;
import com.wosai.upay.proxy.auto.model.ClientTerminal;
import com.wosai.upay.proxy.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

public class ProxyObjectMap {

	private static final Logger logger = LoggerFactory.getLogger(ProxyObjectMap.class);


	private Dao<Map<String, Object>> merchantMapDao;
    private Dao<Map<String, Object>> storeMapDao;
    private Dao<Map<String, Object>> terminalMapDao;

    /**
     * 咨询本地数据库的映射表，Advice。调用方会根据返回的结果调用proxy-core创建相应的业务对象，并且调用set方法更新本地数据库的映射表。
     * @param clientMerchantSn
     * @param clientStoreSn
     * @param clientTerminalSn
     * @return
     */
    public Advice consult(String clientMerchantSn, String clientStoreSn, String clientTerminalSn) {
    	//组织终端查询条件
    	Criteria terminalCriteria=Criteria.where(ClientTerminal.CLIENT_TERMINAL_SN).like(clientTerminalSn);
    	Map<String,Object> terminal=terminalMapDao.filter(terminalCriteria).fetchOne();

    	//组织门店查询条件
    	Criteria storeCriteria=Criteria.where(ClientStore.CLIENT_STORE_SN).like(clientStoreSn);
    	Map<String,Object> store=storeMapDao.filter(storeCriteria).fetchOne();

    	
    	if(terminal==null){
    		if(store==null){
				logger.debug("local mapping consult result: both store and terminal are null. Advice.CREATE_STORE_AND_TERMINAL");
				//判断是否门店和终端都不存在。创建门店和终端
        		return Advice.CREATE_STORE_AND_TERMINAL;
    		}else{
				logger.debug("local mapping consult result: terminal is null. Advice.CREATE_TERMINAL");
				//判断是否门店存在但是终端不存在。创建终端。
    			return Advice.CREATE_TERMINAL;
    		}
    	}
    	
    	
    	if(store==null){
			logger.debug("local mapping consult result: store is null. Advice.CREATE_STORE_AND_MOVE_TERMINAL");
			//判断是否门店不存在但是终端存在。创建门店改变终端归属
    		return Advice.CREATE_STORE_AND_MOVE_TERMINAL;
    	}

    	String tempStoreSn=terminal.get(ClientTerminal.CLIENT_STORE_SN).toString();
    	if(!StringUtil.equals(clientStoreSn, tempStoreSn)){
			logger.debug("local mapping consult result: move terminal. Advice.MOVE_TERMINAL");
			//判断是否门店和终端都存在。需要改变终端归属
    		return Advice.MOVE_TERMINAL;
    	}
		logger.debug("local mapping consult result: 200 Okay. Advice.NOOP");
		//判断是否门店和终端在映射表中存在，并且归属关系没有变化
        return Advice.NOOP;
    }
    
    /**
     * 调用proxy-core创建业务对象成功后，调用set方法更新本地数据库的映射表。
     * @param clientMerchantSn
     * @param clientStoreSn
     * @param storeSn
     * @param clientTerminalSn
     * @param terminalSn
     */
    public void set(String clientMerchantSn,
                    String clientStoreSn,
                    String storeSn,
                    String clientTerminalSn,
                    String terminalSn) {
    	//组织门店查询条件
    	Criteria storeCriteria=Criteria.where(ClientStore.CLIENT_STORE_SN).like(clientStoreSn);
    	Map<String,Object> store=storeMapDao.filter(storeCriteria).fetchOne();
    	if(store==null){
    		store=new HashMap<String,Object>();
    		store.put(ClientStore.ID, UUID.randomUUID().toString());
    		store.put(ClientStore.CLIENT_STORE_SN, clientStoreSn);
    		store.put(ClientStore.STORE_SN, storeSn);
    		store.put(ClientStore.CLIENT_MERCHANT_SN, clientMerchantSn);
    		storeMapDao.save(store);
    	}else{
    		store.put(ClientStore.CLIENT_STORE_SN, clientStoreSn);
    		store.put(ClientStore.CLIENT_MERCHANT_SN, clientMerchantSn);
    		storeMapDao.updatePart(store);
    	}
    	
    	//组织终端查询条件
    	Criteria terminalCriteria=Criteria.where(ClientTerminal.CLIENT_TERMINAL_SN).like(clientTerminalSn);
    	Map<String,Object> terminal=terminalMapDao.filter(terminalCriteria).fetchOne();
    	if(terminal==null){
    		terminal=new HashMap<String,Object>();
    		terminal.put(ClientTerminal.ID, UUID.randomUUID().toString());
        	terminal.put(ClientTerminal.CLIENT_STORE_SN, clientStoreSn);
        	terminal.put(ClientTerminal.CLIENT_MERCHANT_SN, clientMerchantSn);
        	terminal.put(ClientTerminal.CLIENT_TERMINAL_SN, clientTerminalSn);
        	terminal.put(ClientTerminal.TERMINAL_SN, terminalSn);
        	terminalMapDao.save(terminal);
    	}else{
        	terminal.put(ClientTerminal.CLIENT_STORE_SN, clientStoreSn);
        	terminal.put(ClientTerminal.CLIENT_MERCHANT_SN, clientMerchantSn);
        	terminal.put(ClientTerminal.CLIENT_TERMINAL_SN, clientTerminalSn);
        	terminalMapDao.updatePart(terminal);
    	}
    }
    
    /**
     * 调用proxy-core创建业务对象成功后，调用set方法更新本地数据库的映射表（实际的要存储服务端的terminal和store的id信息了）。
     * @param clientMerchantSn
     * @param clientStoreSn
     * @param storeSn
     * @param clientTerminalSn
     * @param terminalSn
     */
    public void setV2(String clientMerchantSn,
                    String clientStoreSn,
                    String storeSn,
                    String clientTerminalSn,
                    String terminalSn,
                    String terminalId,
                    String storeId) {
    	logger.debug(" Update mapping relation ");
    	//组织门店查询条件
    	Criteria storeCriteria=Criteria.where(ClientStore.CLIENT_STORE_SN).like(clientStoreSn);
    	Map<String,Object> store=storeMapDao.filter(storeCriteria).fetchOne();
    	if(store==null){
    		store=new HashMap<String,Object>();
    		store.put(ClientStore.ID, storeId);
    		store.put(ClientStore.CLIENT_STORE_SN, clientStoreSn);
    		store.put(ClientStore.STORE_SN, storeSn);
    		store.put(ClientStore.CLIENT_MERCHANT_SN, clientMerchantSn);
        	logger.debug(" save store ");
    		storeMapDao.save(store);
    	}else{
    		store.put(ClientStore.CLIENT_STORE_SN, clientStoreSn);
    		store.put(ClientStore.CLIENT_MERCHANT_SN, clientMerchantSn);
        	logger.debug(" update store ");
    		storeMapDao.updatePart(store);
    	}
    	
    	//组织终端查询条件
    	Criteria terminalCriteria=Criteria.where(ClientTerminal.CLIENT_TERMINAL_SN).like(clientTerminalSn);
    	Map<String,Object> terminal=terminalMapDao.filter(terminalCriteria).fetchOne();
    	if(terminal==null){
    		terminal=new HashMap<String,Object>();
    		terminal.put(ClientTerminal.ID, terminalId);
        	terminal.put(ClientTerminal.CLIENT_STORE_SN, clientStoreSn);
        	terminal.put(ClientTerminal.CLIENT_MERCHANT_SN, clientMerchantSn);
        	terminal.put(ClientTerminal.CLIENT_TERMINAL_SN, clientTerminalSn);
        	terminal.put(ClientTerminal.TERMINAL_SN, terminalSn);

        	logger.debug(" save terminal ");
        	terminalMapDao.save(terminal);
    	}else{
        	terminal.put(ClientTerminal.CLIENT_STORE_SN, clientStoreSn);
        	terminal.put(ClientTerminal.CLIENT_MERCHANT_SN, clientMerchantSn);
        	terminal.put(ClientTerminal.CLIENT_TERMINAL_SN, clientTerminalSn);
        	logger.debug(" update terminal ");
        	terminalMapDao.updatePart(terminal);
    	}
    }
    
    /**
     * 根据store的client_sn从映射表中查询store的id
     * @param clientMerchantSn
     * @param clientTerminalSn
     * @return
     */
    public String getStoreId(String clientStoreSn) {
    	Map<String,Object> store=getStore(clientStoreSn);
        return store.get(ClientStore.ID).toString();
    }
    
    /**
     * 根据store的client_sn从映射表中查询store的id
     * @param clientMerchantSn
     * @param clientTerminalSn
     * @return
     */
    public String getMerchantId(String clientMerchantSn) {
    	Criteria merchantCriteria=Criteria.where(ClientMerchant.CLIENT_MERCHANT_SN).like(clientMerchantSn);
    	Map<String,Object> merchant=merchantMapDao.filter(merchantCriteria).fetchOne();
        return merchant.get(ClientMerchant.ID).toString();
    }
    
    /**
     * 根据store的client_sn从映射表中查询store的id
     * @param clientMerchantSn
     * @param clientTerminalSn
     * @return
     */
    @Cacheable("get_store")
    public Map<String,Object> getStore(String clientStoreSn) {
    	//组织门店查询条件
    	Criteria storeCriteria=Criteria.where(ClientStore.CLIENT_STORE_SN).like(clientStoreSn);
    	Map<String,Object> store=storeMapDao.filter(storeCriteria).fetchOne();
        return store;
    }
    
    /**
     * 从映射表中查询terminal_sn（系统分配的终端序列号，调用proxy-core创建终端的时候返回）
     * @param clientMerchantSn
     * @param clientTerminalSn
     * @return
     */
    public String getTerminalSn(String clientMerchantSn, String clientTerminalSn) {
    	//组织终端查询条件
    	Criteria terminalCriteria=Criteria.where(ClientTerminal.CLIENT_TERMINAL_SN).like(clientTerminalSn);
    	Map<String,Object> terminal=terminalMapDao.filter(terminalCriteria).fetchOne();
        return terminal.get(ClientTerminal.TERMINAL_SN).toString();
    }
    
    /**
     * 从映射表中查询terminal_sn（系统分配的终端序列号，调用proxy-core创建终端的时候返回）
     * @param clientMerchantSn
     * @param clientTerminalSn
     * @return
     */
    public String getMerchantSn(String clientMerchantSn) {

    	//组织商户查询条件
    	Criteria merchantCriteria=Criteria.where(ClientMerchant.CLIENT_MERCHANT_SN).like(clientMerchantSn);
    	Map<String,Object> merchant=storeMapDao.filter(merchantCriteria).fetchOne();
        return merchant.get(ClientMerchant.MERCHANT_SN).toString();
    }

    public void setStoreMapDao(Dao<Map<String, Object>> dao) {
        this.storeMapDao = dao;
    }
    public void setTerminalMapDao(Dao<Map<String, Object>> dao) {
        this.terminalMapDao = dao;
    }

    public void setMerchantMapDao(Dao<Map<String, Object>> merchantMapDao) {
		this.merchantMapDao = merchantMapDao;
	}

	public static enum Advice {
        NOOP,                           // 门店和终端在映射表中存在，并且归属关系没有变化
        CREATE_STORE_AND_MOVE_TERMINAL, // 门店不存在但是终端存在。创建门店改变终端归属
        CREATE_STORE_AND_TERMINAL,      // 门店和终端都不存在。创建门店和终端
        CREATE_TERMINAL,                // 门店存在但是终端不存在。创建终端。
        MOVE_TERMINAL,                  // 门店和终端都存在。需要改变终端归属
    }
}
