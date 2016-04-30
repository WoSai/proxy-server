package com.wosai.upay.proxy.auto.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
import com.wosai.upay.proxy.util.ZipUtil;

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
		
    	//服务端入库
        try {
            return proxyCore.createStore(request);
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }
    }
    @Override
    public void updateStore(Map<String, Object> request)
            throws ProxyAutoException {
    	//服务端入库
        try {
            proxyCore.updateStore(request);
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }
        
    }
    @Override
    public Map<String, Object> getStore(String sn) throws ProxyAutoException {
        try {
            return proxyCore.getStore(sn);
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }
    }
    @Override
    public Map<String, Object> createTerminal(Map<String, Object> request)
            throws ProxyAutoException {
    	
    	//服务端入库
        try {
            return proxyCore.createTerminal(request);
        }catch(ProxyCoreException ex) {
            throw new ProxyCoreDependencyException(ex.getMessage(), ex);
        }

        
    }
    @Override
    public void updateTerminal(Map<String, Object> request)
            throws ProxyAutoException {
        try {
            proxyCore.updateTerminal(request);
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
		String logDir=logService.getLogDir();
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
			try {

				logger.debug(new StringBuilder(log.getName()).append(" is compressing.").toString());
				
				//获取文件的压缩内容
				String content = ZipUtil.zipByFile(log);
		        
		        //组织参数调用服务端上传接口上传
				Map<String,Object> request=new HashMap<String,Object>();
				request.put(Order.ORDER_LOG, content);
				//从文件名中解析设备编号
				request.put(Order.TERMINAL_SN, log.getName());
				
				logger.debug(new StringBuilder(log.getName()).append(" calling uploadLog api.").toString());
				
				Map<String,Object> result=proxyUpay.uploadLog(request);
				String resultCode = (String)result.get(Response.RESULT_CODE);

				//上传成功后，删除日志文件
				if(resultCode!=null&&resultCode.equals(Response.RESPONSE_CODE_SUCEESS)){
					logger.debug(new StringBuilder(" deleting").append(log.getName()).append(".").toString());
					logService.remove(log);
				}
			} catch (IOException e) {
				logger.debug(new StringBuilder(log.getName()).append(" uploadLog faild.").toString());
				e.printStackTrace();
			}
		}
	}
	
    @Override
    public Map<String, Object> getTerminal(String sn) throws ProxyAutoException {
        try {
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
    	String clientStoreSn=clientStore.get(ClientOrderStore.CLIENT_SN.toString()).toString();
    	String clientTerminalSn=clientTerminal.get(ClientOrderTerminal.CLIENT_SN.toString()).toString();
    	String clientMerchantSn=clientStore.get(ClientOrderStore.CLIENT_MERCHANT_SN.toString()).toString();
    	String merchantId=null,storeSn=null,storeId,terminalSn,terminalId=null;
    	
    	//转成服务端接口所需参数
		Map<String,Object> terminal = clientTerminal;
		Map<String,Object> store = clientStore;
    	
    	//本地映射校验
    	Advice advice=theMap.consult(clientMerchantSn, clientStoreSn, clientTerminalSn);
    	switch (advice) {
		case CREATE_TERMINAL:
			//根据store的client_sn获取id
			storeId=theMap.getStoreId(clientStoreSn);
			terminal.put(Terminal.STORE_ID, storeId);
			terminal = this.createTerminal(terminal);
			logger.debug(" create terminal success.");
			//获取返回结果的终端标识
			terminalSn=terminal.get(Terminal.SN).toString();
			terminalId=terminal.get(Terminal.ID).toString();
	    	theMap.setV2(clientMerchantSn, clientStoreSn, storeSn, clientTerminalSn, terminalSn, terminalId, storeId);
			break;
			
		case MOVE_TERMINAL:
			//获取服务端的sn码
			terminalSn=theMap.getTerminalSn(clientMerchantSn, clientTerminalSn);
			//根据store的client_sn获取id
			storeId=theMap.getStoreId(clientStoreSn);
			terminal.put(Terminal.STORE_ID, storeId);
			//根据服务端的sn码，修改服务端的终端信息
			this.updateTerminal(terminal);
			logger.debug(" update terminal success.");
	    	theMap.setV2(clientMerchantSn, clientStoreSn, storeSn, clientTerminalSn, terminalSn, terminalId, storeId);
			break;
			
		case CREATE_STORE_AND_TERMINAL:
			//调用服务端门店创建接口，并获取返回结果的门店标识
			merchantId=theMap.getMerchantId(clientMerchantSn);
			store.put(Store.MERCHANT_ID, merchantId);
			store=this.createStore(store);
			logger.debug(" create store success.");
			//根据storeid
			storeId=store.get(Store.ID).toString();
			terminal.put(Terminal.STORE_ID, storeId);
			
			//调用服务端创建终端接口，并获取返回结果的终端标识
			terminal=this.createTerminal(terminal);
			logger.debug(" create terminal success.");
			terminalSn=terminal.get(Terminal.SN).toString();
			terminalId=terminal.get(Terminal.ID).toString();
	    	theMap.setV2(clientMerchantSn, clientStoreSn, storeSn, clientTerminalSn, terminalSn, terminalId, storeId);
			break;
			
		case CREATE_STORE_AND_MOVE_TERMINAL:
			//调用服务端门店创建接口，并获取返回结果的门店标识
			store=this.createStore(store);
			logger.debug(" create store success.");
			//根据storeid
			storeId=store.get(Store.ID).toString();
			terminal.put(Terminal.STORE_ID, storeId);
			
			//获取服务端的sn码
			terminalSn=theMap.getTerminalSn(clientMerchantSn, clientTerminalSn);
			//根据服务端的sn码，修改服务端的终端信息
			this.updateTerminal(terminal);
			logger.debug(" update terminal success.");
	    	theMap.setV2(clientMerchantSn, clientStoreSn, storeSn, clientTerminalSn, terminalSn, terminalId, storeId);
			break;

		default:
			//获取服务端的sn码
			terminalSn=theMap.getTerminalSn(clientMerchantSn, clientTerminalSn);
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
