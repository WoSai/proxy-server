package com.wosai.upay.proxy.auto.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wosai.upay.proxy.auto.exception.ProxyAutoException;
import com.wosai.upay.proxy.auto.exception.ProxyCoreDependencyException;
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
import com.wosai.upay.proxy.core.model.Store;
import com.wosai.upay.proxy.core.model.Terminal;
import com.wosai.upay.proxy.core.service.ProxyCoreService;
import com.wosai.upay.proxy.exception.ResponseResolveException;
import com.wosai.upay.proxy.upay.model.TerminalKey;
import com.wosai.upay.proxy.upay.service.ProxyUpayService;
import com.wosai.upay.proxy.util.ResponseUtil;

@Service
public class ProxyAutoServiceImpl implements ProxyAutoService {

    @Autowired
    private ProxyUpayService proxyUpay;
    @Autowired
    private ProxyCoreService proxyCore;
    @Autowired
    private ProxyObjectMap theMap;

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
        return proxyUpay.pay(param);
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
    	return proxyUpay.precreate(param);
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
    	return proxyUpay.query(param);
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
    	return proxyUpay.refund(param);
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
    	return proxyUpay.revoke(param);
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
    	return proxyUpay.cancel(param);
    }
    @Override
    public Map<String, Object> createStore(Map<String, Object> request)
            throws ProxyAutoException {
		
    	//服务端入库
        return proxyCore.createStore(request);
    }
    @Override
    public void updateStore(Map<String, Object> request)
            throws ProxyAutoException {
    	//服务端入库
    	proxyCore.updateStore(request);
        
    }
    @Override
    public Map<String, Object> getStore(String sn) throws ProxyAutoException {
        return proxyCore.getStore(sn);
    }
    @Override
    public Map<String, Object> createTerminal(Map<String, Object> request)
            throws ProxyAutoException {
    	
    	//服务端入库
        return proxyCore.createTerminal(request);
        
    }
    @Override
    public void updateTerminal(Map<String, Object> request)
            throws ProxyAutoException {
        proxyCore.updateTerminal(request);
    }
    @Override
    public Map<String, Object> activateTerminal(Map<String, Object> request)
            throws ProxyAutoException {
    	Map<String, Object> response=proxyCore.activateTerminal(request);
    	try{
	    	Map<String, Object> terminal=ResponseUtil.resolveCore(response);
	    	String terminalSn=terminal.get(TerminalKey.TERMINAL_SN).toString();
	    	String terminalKey=terminal.get(TerminalKey.TERMINAL_KEY).toString();
	    	proxyUpay.init(terminalSn, terminalKey);
	    	//服务端入库
	        return response;
    	} catch (ResponseResolveException e) {
			throw new ProxyCoreDependencyException("activate terminal faild");
		}
        
    }
    @Override
    public Map<String, Object> getTerminal(String sn) throws ProxyAutoException {
        return proxyCore.getTerminal(sn);
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
    	String clientMerchantSn=null,storeSn=null,terminalSn=null;
    	
    	//转成服务端接口所需参数
		Map<String,Object> terminal=this.parseTerminalAutoToCore(clientTerminal);
		Map<String,Object> store=this.parseTerminalAutoToCore(clientStore);
    	
    	//本地映射校验
    	Advice advice=theMap.consult(clientMerchantSn, clientStoreSn, clientTerminalSn);
    	switch (advice) {
		case CREATE_TERMINAL:
			terminal=this.parseAndCreateTerminal(terminal);
			//获取返回结果的终端标识
			terminalSn=terminal.get(Terminal.SN).toString();
	    	theMap.set(clientMerchantSn, clientStoreSn, storeSn, clientTerminalSn, terminalSn);
			break;
		case MOVE_TERMINAL:
			//获取服务端的sn码
			terminalSn=theMap.getTerminalSn(clientMerchantSn, clientTerminalSn);
			//根据服务端的sn码，修改服务端的终端信息
			this.updateTerminal(terminal);
	    	theMap.set(clientMerchantSn, clientStoreSn, storeSn, clientTerminalSn, terminalSn);
			break;
		case CREATE_STORE_AND_TERMINAL:
			//调用服务端门店创建接口，并获取返回结果的门店标识
			store=this.parseAndCreateStore(store);
			storeSn=store.get(Store.SN).toString();
			
			//调用服务端创建终端接口，并获取返回结果的终端标识
			terminal=this.parseAndCreateTerminal(terminal);
			terminalSn=terminal.get(Terminal.SN).toString();
	    	theMap.set(clientMerchantSn, clientStoreSn, storeSn, clientTerminalSn, terminalSn);
			break;
		case CREATE_STORE_AND_MOVE_TERMINAL:
			//调用服务端门店创建接口，并获取返回结果的门店标识
			store=this.parseAndCreateStore(store);
			storeSn=store.get(Store.SN).toString();
			
			//获取服务端的sn码
			terminalSn=theMap.getTerminalSn(clientMerchantSn, clientTerminalSn);
			//根据服务端的sn码，修改服务端的终端信息
			this.updateTerminal(terminal);
	    	theMap.set(clientMerchantSn, clientStoreSn, storeSn, clientTerminalSn, terminalSn);
			break;

		default:
			//获取服务端的sn码
			terminalSn=theMap.getTerminalSn(clientMerchantSn, clientTerminalSn);
		}
    	request.put(ClientOrderPay.TERMINAL_SN.toString(), terminalSn);
    }
    
    /**
     * 创建并解析门店数据
     * @param request
     * @return
     * @throws ProxyAutoException
     */
    private Map<String,Object> parseAndCreateStore(Map<String,Object> request) throws ProxyAutoException {

    	Map<String,Object> response=this.createStore(request);
    	try {
			return ResponseUtil.resolveCore(response);
		} catch (ResponseResolveException e) {
			throw new ProxyCoreDependencyException("create store faild");
		}
    }
    
    /**
     * 创建并解析门店数据
     * @param request
     * @return
     * @throws ProxyAutoException
     */
    private Map<String,Object> parseAndCreateTerminal(Map<String,Object> request) throws ProxyAutoException {

    	Map<String,Object> response=this.createTerminal(request);
    	try {
			return ResponseUtil.resolveCore(response);
		} catch (ResponseResolveException e) {
			throw new ProxyCoreDependencyException("create terminal faild");
		}
    }
    
    /**
     * 解析auto的门店参数转化成core需要的门店参数
     * @param request
     * @return
     */
    public Map<String,Object> parseStoreAutoToCore(Map<String,Object> src){
    	Map<String,Object> dest=new HashMap<String,Object>();
    	ClientOrderStore[] values=ClientOrderStore.values();
    	for(ClientOrderStore value:values){
    		this.transferMap(src, dest, value.getValue(), value.getMap());
    	}
    	return dest;
    }

    /**
     * 解析auto的终端参数转化成core需要的终端参数
     * @param request
     * @return
     */
    public Map<String,Object> parseTerminalAutoToCore(Map<String,Object> src){
    	Map<String,Object> dest=new HashMap<String,Object>();
    	ClientOrderTerminal[] values=ClientOrderTerminal.values();
    	for(ClientOrderTerminal value:values){
    		this.transferMap(src, dest, value.getValue(), value.getMap());
    	}
    	return dest;
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
