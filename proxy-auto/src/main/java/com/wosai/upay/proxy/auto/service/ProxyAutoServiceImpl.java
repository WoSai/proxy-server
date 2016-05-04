package com.wosai.upay.proxy.auto.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wosai.upay.proxy.auto.exception.ParameterValidationException;
import com.wosai.upay.proxy.auto.exception.ProxyAutoException;
import com.wosai.upay.proxy.auto.exception.ProxyCoreDependencyException;
import com.wosai.upay.proxy.auto.exception.ProxyUpayDependencyException;
import com.wosai.upay.proxy.auto.model.ClientOrder;
import com.wosai.upay.proxy.auto.model.ClientOrderCancel;
import com.wosai.upay.proxy.auto.model.ClientOrderPay;
import com.wosai.upay.proxy.auto.model.ClientOrderPrecreate;
import com.wosai.upay.proxy.auto.model.ClientOrderQuery;
import com.wosai.upay.proxy.auto.model.ClientOrderRefund;
import com.wosai.upay.proxy.auto.model.ClientOrderRevoke;
import com.wosai.upay.proxy.auto.model.ClientOrderStore;
import com.wosai.upay.proxy.auto.model.ClientOrderTerminal;
import com.wosai.upay.proxy.auto.service.ProxyObjectMap.Advice;
import com.wosai.upay.proxy.core.exception.ProxyCoreException;
import com.wosai.upay.proxy.core.model.Store;
import com.wosai.upay.proxy.core.model.Terminal;
import com.wosai.upay.proxy.core.service.ProxyCoreService;
import com.wosai.upay.proxy.exception.ResponseResolveException;
import com.wosai.upay.proxy.model.Response;
import com.wosai.upay.proxy.upay.exception.ProxyUpayException;
import com.wosai.upay.proxy.upay.model.Order;
import com.wosai.upay.proxy.upay.model.TerminalKey;
import com.wosai.upay.proxy.upay.service.ProxyUpayService;

@Service
public class ProxyAutoServiceImpl implements ProxyAutoService {
	
    private static final Logger logger = LoggerFactory.getLogger(ProxyAutoServiceImpl.class); 

    @Autowired
    private ProxyUpayService proxyUpay;
    @Autowired
    private ProxyCoreService proxyCore;
    @Autowired
    private ProxyObjectMap theMap;
    @Autowired
    private LogService logService; 

    @Override
    public Map<String, Object> pay(Map<String, Object> request)
            throws ProxyAutoException {
    	//检查本地映射
    	this.checkObjectMap(request);
    	//参数过滤，过滤多余的参数
    	Map<String,Object> param=new HashMap<String,Object>();
    	ClientOrderPay[] values=ClientOrderPay.values();
    	for(ClientOrderPay value:values){
    		this.transferMap(request, param, value.getValue(), value.getMap());
    	}
    	try {
    	    return proxyUpay.pay(param);
    	}catch(ProxyUpayException ex) {
    	    throw new ProxyUpayDependencyException(ex.getMessage(), ex);
    	}
    }
    @Override
    public Map<String, Object> precreate(Map<String, Object> request)
            throws ProxyAutoException {
    	//检查本地映射
    	this.checkObjectMap(request);
    	//参数过滤，过滤多余的参数
    	Map<String,Object> param=new HashMap<String,Object>();
    	ClientOrderPrecreate[] values=ClientOrderPrecreate.values();
    	for(ClientOrderPrecreate value:values){
    		this.transferMap(request, param, value.getValue(), value.getMap());
    	}
    	try {
    	    return proxyUpay.precreate(param);
    	}catch(ProxyUpayException ex) {
    	    throw new ProxyUpayDependencyException(ex.getMessage(), ex);
    	}
    }
    @Override
    public Map<String, Object> query(Map<String, Object> request)
            throws ProxyAutoException {
    	//检查本地映射
    	this.checkObjectMap(request);
    	//参数过滤，过滤多余的参数
    	Map<String,Object> param=new HashMap<String,Object>();
    	ClientOrderQuery[] values=ClientOrderQuery.values();
    	for(ClientOrderQuery value:values){
    		this.transferMap(request, param, value.getValue(), value.getMap());
    	}
    	try {
    	    return proxyUpay.query(param);
    	}catch(ProxyUpayException ex) {
    	    throw new ProxyUpayDependencyException(ex.getMessage(), ex);
    	}
    }
    @Override
    public Map<String, Object> refund(Map<String, Object> request)
            throws ProxyAutoException {
    	//检查本地映射
    	this.checkObjectMap(request);
    	//参数过滤，过滤多余的参数
    	Map<String,Object> param=new HashMap<String,Object>();
    	ClientOrderRefund[] values=ClientOrderRefund.values();
    	for(ClientOrderRefund value:values){
    		this.transferMap(request, param, value.getValue(), value.getMap());
    	}
    	try {
    	    return proxyUpay.refund(param);
    	}catch(ProxyUpayException ex) {
    	    throw new ProxyUpayDependencyException(ex.getMessage(), ex);
    	}
    }
    @Override
    public Map<String, Object> revoke(Map<String, Object> request)
            throws ProxyAutoException {
    	//检查本地映射
    	this.checkObjectMap(request);
    	//参数过滤，过滤多余的参数
    	Map<String,Object> param=new HashMap<String,Object>();
    	ClientOrderRevoke[] values=ClientOrderRevoke.values();
    	for(ClientOrderRevoke value:values){
    		this.transferMap(request, param, value.getValue(), value.getMap());
    	}
    	try {
    	    return proxyUpay.revoke(param);
    	}catch(ProxyUpayException ex) {
    	    throw new ProxyUpayDependencyException(ex.getMessage(), ex);
    	}
    }
    @Override
    public Map<String, Object> cancel(Map<String, Object> request)
            throws ProxyAutoException {
    	//检查本地映射
    	this.checkObjectMap(request);
    	//参数过滤，过滤多余的参数
    	Map<String,Object> param=new HashMap<String,Object>();
    	ClientOrderCancel[] values=ClientOrderCancel.values();
    	for(ClientOrderCancel value:values){
    		this.transferMap(request, param, value.getValue(), value.getMap());
    	}
    	try {
    	    return proxyUpay.cancel(param);
    	} catch(ProxyUpayException ex) {
    	    throw new ProxyUpayDependencyException(ex.getMessage(), ex);
    	}
    }
    @Override
    public Map<String, Object> createStore(Map<String, Object> request)
            throws ProxyAutoException {
		try{
			//获取获取本地商家信息
	    	String clientMerchantSn=request.get(ClientOrderStore.CLIENT_MERCHANT_SN.toString()).toString();
			String merchantId=theMap.getMerchantId(clientMerchantSn);
			request.put(Store.MERCHANT_ID, merchantId);
		}catch(Exception e){
			throw new ParameterValidationException("client_merchant_sn无效");
		}
    	
    	//服务端入库
        try {
        	Map<String,Object> param=new HashMap<String,Object>();
        	ClientOrderStore[] values=ClientOrderStore.values();
        	for(ClientOrderStore value:values){
        		this.transferMap(request, param, value.getValue(), value.getMap());
        	}
        	Map<String,Object> result=proxyCore.createStore(param);
        	
			//本地入库
			String storeId=result.get(Store.ID).toString();
			String storeSn=result.get(Store.SN).toString();
	    	String clientStoreSn=request.get(ClientOrderStore.CLIENT_SN.toString()).toString();
	    	String clientMerchantSn=request.get(ClientOrderStore.CLIENT_MERCHANT_SN.toString()).toString();
        	if(storeId!=null){
            	theMap.saveStore(storeId, clientMerchantSn, clientStoreSn, storeSn);
        	}
        	
            return result;
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }
    }
    @Override
    public void updateStore(Map<String, Object> request)
            throws ProxyAutoException {
    	try{
    		//根据clientStoreId获取storeId
    		String clientStoreSn=(String)request.get(ClientOrderStore.CLIENT_SN.toString());
    		String storeId = theMap.getStoreId(clientStoreSn);
    		request.put(Store.ID, storeId);
    		
			//获取获取本地商家信息
    		String clientMerchantSn=(String)request.get(ClientOrderStore.CLIENT_MERCHANT_SN.toString());
    		//更新时,merchantId可以为空
    		if(clientMerchantSn!=null){
    			String merchantId=theMap.getMerchantId(clientMerchantSn);
    			request.put(Store.MERCHANT_ID, merchantId);
    		}
		}catch(Exception e){
			throw new ParameterValidationException("client_merchant_sn无效");
		}
    	
    	//服务端入库
        try {
        	Map<String,Object> param=new HashMap<String,Object>();
        	ClientOrderStore[] values=ClientOrderStore.values();
        	for(ClientOrderStore value:values){
        		this.transferMap(request, param, value.getValue(), value.getMap());
        	}
            proxyCore.updateStore(param);
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }
        //本地库不做任何操作
    }
    @Override
    public Map<String, Object> getStore(Map<String, Object> request) throws ProxyAutoException {
        try {
        	String sn = request.get(Store.CLIENT_SN).toString();
            return proxyCore.getStore(sn);
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }
    }
    @Override
    public Map<String, Object> createTerminal(Map<String, Object> request)
            throws ProxyAutoException {
    	String clientStoreSn=null,clientTerminalSn=null,clientMerchantSn=null;
    	try{
	    	//初始化本地映射的参数
			clientStoreSn=request.get(ClientOrderTerminal.CLIENT_STORE_SN.toString()).toString();
			String storeSn=theMap.getStoreSn(clientStoreSn);
			request.put(Terminal.STORE_SN, storeSn);
    	}catch(Exception e){
			throw new ParameterValidationException("clientStoreSn无效");
    	}
    	
    	
        try {
        	//服务端入库
        	//参数过滤，过滤多余的参数
        	Map<String,Object> param=new HashMap<String,Object>();
        	ClientOrderTerminal[] values=ClientOrderTerminal.values();
        	for(ClientOrderTerminal value:values){
        		this.transferMap(request, param, value.getValue(), value.getMap());
        	}
        	Map<String, Object> result = proxyCore.createTerminal(param);
        	

        	//本地入库
			String terminalSn=result.get(Terminal.SN).toString();
			String terminalId=result.get(Terminal.ID).toString();
	    	clientTerminalSn = request.get(ClientOrderTerminal.CLIENT_SN.toString()).toString();
	    	clientMerchantSn = theMap.getClientMerchantSn(clientStoreSn);
        	theMap.saveTerminal(terminalId, clientMerchantSn, clientStoreSn, clientTerminalSn, terminalSn);
        	
			try{
		    	//初始化终端秘钥
				String secret = result.get(Terminal.SECRET).toString();
				proxyUpay.init(terminalSn, secret);
			}catch(Exception e){
				logger.debug("init secret faild.");
				throw new ProxyUpayDependencyException("init secret faild.");
			}
            return result;
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }

        
    }
    @Override
    public void updateTerminal(Map<String, Object> request)
            throws ProxyAutoException {

    	String clientStoreSn=null,clientTerminalSn=null,clientMerchantSn=null,terminalId=null;

    	try{
	    	//初始化本地映射的参数
    		
    		//根据clientTerminalSn获取terminalId
        	clientTerminalSn=(String)request.get(ClientOrderTerminal.CLIENT_SN.toString());
    		terminalId = theMap.getTerminalId(clientTerminalSn);
    		request.put(Terminal.ID, terminalId);
    		
    		//根据clientStoreSn获取storeId
    		clientStoreSn=(String)request.get(ClientOrderTerminal.CLIENT_STORE_SN.toString());
	    	if(clientStoreSn!=null){
	    		String storeId=theMap.getStoreId(clientStoreSn);
	    		request.put(Terminal.STORE_ID, storeId);
	    		
	        	clientMerchantSn = theMap.getClientMerchantSn(clientStoreSn);
	    	}
	
		}catch(Exception e){
			throw new ParameterValidationException("clientStoreSn无效");
		}
		
        try {
        	//服务端入库
        	//参数过滤，过滤多余的参数
        	Map<String,Object> param=new HashMap<String,Object>();
        	ClientOrderTerminal[] values=ClientOrderTerminal.values();
        	for(ClientOrderTerminal value:values){
        		this.transferMap(request, param, value.getValue(), value.getMap());
        	}
            proxyCore.updateTerminal(param);
            
            
            //本地入库
        	clientTerminalSn=request.get(ClientOrderTerminal.CLIENT_SN.toString()).toString();
        	theMap.updateTerminal(clientMerchantSn, clientStoreSn, clientTerminalSn);
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }

    }
    @Override
    public Map<String, Object> activateTerminal(Map<String, Object> request)
            throws ProxyAutoException {

        try {
            Map<String, Object> terminal = proxyCore.activateTerminal(request);
            String terminalSn=terminal.get(TerminalKey.TERMINAL_SN).toString();
            String terminalKey=terminal.get(TerminalKey.TERMINAL_KEY).toString();
            proxyUpay.init(terminalSn, terminalKey);
            return terminal;
            
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }catch(ProxyUpayException ex) {
            throw new ProxyUpayDependencyException(ex.getMessage(), ex);
        }

    }

	@Override
	public void uploadLog() throws ProxyAutoException {
		Map<String,String> logMap=logService.list();
		Iterator<String> it=logMap.keySet().iterator();
		while(it.hasNext()) {
		        
	        //组织参数调用服务端上传接口上传
			Map<String,Object> request=new HashMap<String,Object>();
			String terminalSn=it.next();
			String content = logMap.get(terminalSn);
			request.put(Order.ORDER_LOG, content);
			request.put(Order.TERMINAL_SN, terminalSn);
			
			logger.debug(new StringBuilder(terminalSn).append(" calling uploadLog api.").toString());

			Map<String,Object> result=proxyUpay.uploadLog(request);
			String resultCode = (String)result.get(Response.RESULT_CODE);

			//上传成功后，删除日志文件
			if(resultCode!=null&&resultCode.equals(Response.RESULT_CODE_SUCEESS)){
				logger.debug(new StringBuilder(" deleting").append(terminalSn).append(".").toString());
				logService.remove(terminalSn);
			}else{
				logger.debug(new StringBuilder(terminalSn).append(" uploadLog faild.").toString());
			}
		}
	}
	
    @Override
    public Map<String, Object> getTerminal(Map<String, Object> request) throws ProxyAutoException {
        try {
        	String sn = request.get(Terminal.CLIENT_SN).toString();
            return proxyCore.getTerminal(sn);
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }

    }

    /**
     * 检查本地映射，并做相关数据同步
     * @param request
     * @throws ResponseResolveException 
     */
    @SuppressWarnings("unchecked")
    public void checkObjectMap(Map<String,Object> request) throws ProxyAutoException {
	
		//获取交易参数中的门店和终端信息
		Map<String,Object> clientTerminal=(Map<String, Object>)request.get(ClientOrder.CLIENT_TERMINAL);
		Map<String,Object> clientStore=(Map<String, Object>)request.get(ClientOrder.CLIENT_STORE);
		
    	String clientStoreSn=(String)clientStore.get(ClientOrderStore.CLIENT_SN.toString());
    	String clientTerminalSn=(String)clientTerminal.get(ClientOrderTerminal.CLIENT_SN.toString());
    	String clientMerchantSn=(String)clientStore.get(ClientOrderStore.CLIENT_MERCHANT_SN.toString());
    	
    	String terminalSn=null;
    	
    	//转成服务端接口所需参数
		Map<String,Object> terminal = null;
		Map<String,Object> store = null;
    	
    	//本地映射校验
    	Advice advice=theMap.consult(clientMerchantSn, clientStoreSn, clientTerminalSn);
    	switch (advice) {
		case CREATE_TERMINAL:

			try{
				//创建终端
				clientTerminal.put(ClientOrderTerminal.CLIENT_STORE_SN.toString(), clientStoreSn);
				terminal=this.createTerminal(clientTerminal);
			}catch(Exception e){
				logger.debug("create terminal faild.");
				throw new ProxyCoreDependencyException("create terminal faild.", e);
			}
			logger.debug(" create terminal success.");
			
			//获取返回结果的终端标识
			terminalSn=terminal.get(Terminal.SN).toString();
			break;
			
		case MOVE_TERMINAL:

			try{
				//更新终端
				clientTerminal.put(ClientOrderTerminal.CLIENT_STORE_SN.toString(), clientStoreSn);
				this.updateTerminal(clientTerminal);
			}catch(Exception e){
				logger.debug("update terminal faild.");
				throw new ProxyCoreDependencyException("update terminal faild.", e);
			}
			logger.debug(" update terminal success.");
			
			//获取本地的sn码
			terminalSn=theMap.getTerminalSn(clientTerminalSn);
			break;
			
		case CREATE_STORE_AND_TERMINAL:
			try{
				//创建门店
				store=this.createStore(clientStore);
			}catch(Exception e){
				logger.debug("create store faild.");
				throw new ProxyCoreDependencyException("create store faild.", e);
			}

			try{
				//创建终端
				clientTerminal.put(ClientOrderTerminal.CLIENT_STORE_SN.toString(), clientStoreSn);
				terminal=this.createTerminal(clientTerminal);
			}catch(Exception e){
				logger.debug("create terminal faild.");
				throw new ProxyCoreDependencyException("create terminal faild.", e);
			}
			logger.debug(" create terminal success.");
			
			//获取服务端sn码
			terminalSn=terminal.get(Terminal.SN).toString();
			break;
			
		case CREATE_STORE_AND_MOVE_TERMINAL:
			try{
				//创建门店
				store=this.createStore(clientStore);
			}catch(Exception e){
				logger.debug("create store faild.");
				throw new ProxyCoreDependencyException("create store faild.", e);
			}
			logger.debug("create store success.");
			
			try{
				//更新终端
				clientTerminal.put(ClientOrderTerminal.CLIENT_STORE_SN.toString(), clientStoreSn);
				this.updateTerminal(clientTerminal);
			}catch(Exception e){
				logger.debug("update terminal faild.");
				throw new ProxyCoreDependencyException("update terminal faild.", e);
			}
			logger.debug(" update terminal success.");
			
			//获取本地的sn码
			terminalSn=theMap.getTerminalSn(clientTerminalSn);
			break;

		default:
			//获取本地的sn码
			terminalSn=theMap.getTerminalSn(clientTerminalSn);
			logger.debug(" no updates.");
			
		}
    	request.put(ClientOrderPay.TERMINAL_SN.toString(), terminalSn);
    	
    	//把设备唯一标识保存到线程临时变量中，供日志组件使用
    	logService.setTerminalSn(terminalSn);
    }
    
    
    /**
     * map转存
     * @param src
     * @param dest
     * @param srcKey
     * @param destKey
     */
    public void transferMap(Map<String,Object> src,Map<String,Object> dest,String srcKey,String destKey){
    	if(src!=null&&dest!=null&&srcKey!=null&&destKey!=null&&src.get(srcKey)!=null){
        	dest.put(destKey, src.get(srcKey));
    	}
    	
    }
}
