package com.wosai.upay.proxy.auto.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.wosai.data.dao.Criteria;
import com.wosai.data.dao.Dao;
import com.wosai.data.util.CollectionUtil;
import com.wosai.upay.proxy.auto.model.ClientMerchant;
import com.wosai.upay.proxy.auto.model.ClientStore;
import com.wosai.upay.proxy.auto.model.ClientTerminal;
import com.wosai.upay.proxy.util.StringUtil;

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
    public int consult(String clientMerchantSn, String clientStoreSn, String clientTerminalSn) {
    	
    	//组织终端查询条件
    	Criteria terminalCriteria=Criteria.where(ClientTerminal.CLIENT_TERMINAL_SN).is(clientTerminalSn);
    	Map<String,Object> terminal=terminalMapDao.filter(terminalCriteria).fetchOne();

    	//组织门店查询条件
    	Criteria storeCriteria=Criteria.where(ClientStore.CLIENT_STORE_SN).is(clientStoreSn);
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
    @Deprecated
    public void set(String clientMerchantSn,
                    String clientStoreSn,
                    String storeSn,
                    String clientTerminalSn,
                    String terminalSn) {
    	//组织门店查询条件
    	Criteria storeCriteria=Criteria.where(ClientStore.CLIENT_STORE_SN).is(clientStoreSn);
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
    	Criteria terminalCriteria=Criteria.where(ClientTerminal.CLIENT_TERMINAL_SN).is(clientTerminalSn);
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
     * 保存门店
     * @param id
     * @param clientMerchantSn
     * @param clientStoreSn
     * @param storeSn
     */
    public void saveStore(String id,String clientMerchantSn,String clientStoreSn,String storeSn){
		Map<String,Object> store=new HashMap<String,Object>();
		store.put(ClientStore.ID, id);
		store.put(ClientStore.CLIENT_STORE_SN, clientStoreSn);
		store.put(ClientStore.STORE_SN, storeSn);
		store.put(ClientStore.CLIENT_MERCHANT_SN, clientMerchantSn);
    	logger.debug(" save store ");
		storeMapDao.save(store);
    }
    
    /**
     * 保存终端
     * @param terminalId
     * @param clientMerchantSn
     * @param clientStoreSn
     * @param clientTerminalSn
     * @param terminalSn
     */
    public void saveTerminal(String terminalId,String clientMerchantSn,String clientStoreSn,String clientTerminalSn,String terminalSn){
		Map<String,Object> terminal=new HashMap<String,Object>();
		terminal.put(ClientTerminal.ID, terminalId);
    	terminal.put(ClientTerminal.CLIENT_STORE_SN, clientStoreSn);
    	terminal.put(ClientTerminal.CLIENT_MERCHANT_SN, clientMerchantSn);
    	terminal.put(ClientTerminal.CLIENT_TERMINAL_SN, clientTerminalSn);
    	terminal.put(ClientTerminal.TERMINAL_SN, terminalSn);

    	logger.debug(" save terminal ");
    	terminalMapDao.save(terminal);
    }
    
    /**
     * 更新终端
     * @param clientMerchantSn
     * @param clientStoreSn
     * @param clientTerminalSn
     */
    public void updateTerminal(String clientMerchantSn,String clientStoreSn,String clientTerminalSn){
    	if(clientStoreSn!=null){
        	//组织终端查询条件
        	Criteria terminalCriteria=Criteria.where(ClientTerminal.CLIENT_TERMINAL_SN).is(clientTerminalSn);
        	Map<String,Object> terminal=terminalMapDao.filter(terminalCriteria).fetchOne();
        	
        	terminal.put(ClientTerminal.CLIENT_STORE_SN, clientStoreSn);
        	terminal.put(ClientTerminal.CLIENT_MERCHANT_SN, clientMerchantSn);
        	terminal.put(ClientTerminal.CLIENT_TERMINAL_SN, clientTerminalSn);

        	logger.debug(" update terminal ");
        	terminalMapDao.updatePart(terminal);
    	}
    }
    
    /**
     * 根据store的client_sn从映射表中查询store的sn
     * @param clientStoreSn
     * @return
     */
    public String getStoreSn(String clientStoreSn) {
    	Map<String,Object> store=getStore(clientStoreSn);
        return store.get(ClientStore.STORE_SN).toString();
    }
    
    /**
     * 根据store的client_sn从映射表中查询store的id
     * @param clientStoreSn
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
    @Cacheable("get_store")
    public Map<String,Object> getStore(String clientStoreSn) {
    	//组织门店查询条件
    	Criteria storeCriteria=Criteria.where(ClientStore.CLIENT_STORE_SN).is(clientStoreSn);
    	Map<String,Object> store=storeMapDao.filter(storeCriteria).fetchOne();
        return store;
    }

    
    /**
     * 从映射表中查询client_store_sn
     * @param clientTerminalSn
     * @return
     */
    public String getClientStoreSn(String clientTerminalSn) {
        return this.getTerminal(clientTerminalSn).get(ClientTerminal.CLIENT_STORE_SN).toString();
    }
    
    /**
     * 从映射表中查询terminal_sn（系统分配的终端序列号，调用proxy-core创建终端的时候返回）
     * @param clientTerminalSn
     * @return
     */
    public String getTerminalSn(String clientTerminalSn) {
        return this.getTerminal(clientTerminalSn).get(ClientTerminal.TERMINAL_SN).toString();
    }
    
    /**
     * 从映射表中查询terminal_sn（系统分配的终端序列号，调用proxy-core创建终端的时候返回）
     * @param clientMerchantSn
     * @param clientTerminalSn
     * @return
     */
    public String getTerminalId(String clientTerminalSn) {
        return this.getTerminal(clientTerminalSn).get(ClientTerminal.ID).toString();
    }
    
    /**
     * 根据store的client_sn从映射表中查询store的id
     * @param clientMerchantSn
     * @param clientTerminalSn
     * @return
     */
    @Cacheable("get_terminal")
    public Map<String,Object> getTerminal(String clientTerminalSn) {
    	//组织终端查询条件
    	Criteria terminalCriteria=Criteria.where(ClientTerminal.CLIENT_TERMINAL_SN).is(clientTerminalSn);
    	Map<String,Object> terminal=terminalMapDao.filter(terminalCriteria).fetchOne();
        return terminal;
    }

    /**
     * 根据clientStoreSn获取clientMerchantSn
     * @param clientStoreSn
     * @return
     */
    public String getClientMerchantSn(String clientStoreSn) {
    	Map<String,Object> store=getStore(clientStoreSn);
        return store.get(ClientStore.CLIENT_MERCHANT_SN).toString();
    }

    @Cacheable(value="terminals")
    public String getTerminalSn(String clientMerchantSn, String clientStoreSn, String clientTerminalSn) {
        Map<String, Object> terminal = terminalMapDao.filter(Criteria.where(ClientTerminal.CLIENT_MERCHANT_SN).is(clientMerchantSn)
                                                             .with(ClientTerminal.CLIENT_STORE_SN).is(clientStoreSn)
                                                             .with(ClientTerminal.CLIENT_TERMINAL_SN).is(clientTerminalSn)).fetchOne();
        if (terminal != null) {
            return (String)terminal.get(ClientTerminal.TERMINAL_SN);
        }else{
            return null;
        }
    }
    
    public void setStoreMap(String clientMerchantSn, String clientStoreSn, String storeSn) {
        Map<String, Object> store = (Map <String,Object>)CollectionUtil.hashMap(ClientStore.ID, UUID.randomUUID().toString());
        store.put(ClientStore.CLIENT_MERCHANT_SN, clientMerchantSn);
        store.put(ClientStore.CLIENT_STORE_SN, clientStoreSn);
        store.put(ClientStore.STORE_SN, storeSn);
        storeMapDao.save(store);
    }

    @Transactional(isolation=Isolation.REPEATABLE_READ)
    public synchronized void setTerminalMap(String clientMerchantSn, String clientStoreSn, String clientTerminalSn, String terminalSn) {
        Map<String, Object> terminal = terminalMapDao.filter(Criteria.where(ClientTerminal.CLIENT_MERCHANT_SN).is(clientMerchantSn)
                                                             .with(ClientTerminal.CLIENT_TERMINAL_SN).is(clientTerminalSn)).fetchOne();
        if (terminal == null) {
            terminal = CollectionUtil.hashMap(ClientTerminal.ID, UUID.randomUUID().toString());
            terminal.put(ClientTerminal.CLIENT_MERCHANT_SN, clientMerchantSn);
            terminal.put(ClientTerminal.CLIENT_STORE_SN, clientStoreSn);
            terminal.put(ClientTerminal.CLIENT_TERMINAL_SN, clientTerminalSn);
            terminal.put(ClientTerminal.TERMINAL_SN, terminalSn);
            terminalMapDao.save(terminal);
        }else {
            terminal.put(ClientTerminal.CLIENT_STORE_SN, clientStoreSn);
            terminalMapDao.updatePart(terminal);
        }
    }

    @CacheEvict(value="terminals")
    public void evictTerminalSnCache(String clientMerchantSn, String clientStoreSn, String clientTerminalSn) {
    }

    /**
     * 根据store的client_sn从映射表中查询store的id
     * @param clientMerchantSn
     * @param clientTerminalSn
     * @return
     */
    public String getMerchantId(String clientMerchantSn) {
        return this.getMerchant(clientMerchantSn).get(ClientMerchant.ID).toString();
    }
    
    /**
     * 获取商户
     * @param clientTerminalSn
     * @return
     */
    @Cacheable("get_merchant")
    public Map<String,Object> getMerchant(String clientMerchantSn) {
    	//如果没有传递merchantSn，查询第一条
    	if(clientMerchantSn==null){
    		clientMerchantSn="";
		}
    	//组织商户查询条件
    	Criteria merchantCriteria=Criteria.where(ClientMerchant.CLIENT_MERCHANT_SN).is(clientMerchantSn);
    	Map<String,Object> merchant=merchantMapDao.filter(merchantCriteria).fetchOne();
        return merchant;
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

	public static class Advice {
        public static final int NOOP = 0;                           // 门店和终端在映射表中存在，并且归属关系没有变化
        public static final int CREATE_STORE_AND_MOVE_TERMINAL = 1; // 门店不存在但是终端存在。创建门店改变终端归属
        public static final int CREATE_STORE_AND_TERMINAL = 2;      // 门店和终端都不存在。创建门店和终端
        public static final int CREATE_TERMINAL = 3;                // 门店存在但是终端不存在。创建终端。
        public static final int MOVE_TERMINAL = 4;                  // 门店和终端都存在。需要改变终端归属
        
        private int operation;
        private String storeSn;
        private String terminalSn;
        
        public Advice(int operation, String storeSn, String terminalSn) {
            this.operation = operation;
            this.storeSn = storeSn;
            this.terminalSn = terminalSn;
        }

        public int getOperation() {
            return operation;
        }

        public String getStoreSn() {
            return storeSn;
        }

        public String getTerminalSn() {
            return terminalSn;
        }
        
    }
}
