package com.wosai.upay.proxy.auto.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wosai.data.dao.common.TimedDaoBase;
import com.wosai.upay.proxy.auto.exception.ProxyAutoException;
import com.wosai.upay.proxy.auto.model.ClientStore;
import com.wosai.upay.proxy.auto.model.ClientTerminal;
import com.wosai.upay.proxy.core.model.Store;
import com.wosai.upay.proxy.core.model.Terminal;
import com.wosai.upay.proxy.core.service.ProxyCoreService;
import com.wosai.upay.proxy.upay.service.ProxyUpayService;

@Service
public class ProxyAutoServiceImpl implements ProxyAutoService {

    @Autowired
    private ProxyUpayService proxyUpay;
    @Autowired
    private ProxyCoreService proxyCore;
    @Autowired
    private ProxyObjectMap theMap;
    
    @Resource(name="storeMapDao")
    private TimedDaoBase storeMapDao;
    
    @Resource(name="terminalMapDao")
    private TimedDaoBase terminalMapDao;
    
    @Autowired
    private ProxyCoreService proxyCoreService;

    @Override
    public Map<String, Object> pay(Map<String, Object> request)
            throws ProxyAutoException {

        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Map<String, Object> precreate(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Map<String, Object> query(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Map<String, Object> refund(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Map<String, Object> revoke(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Map<String, Object> createStore(Map<String, Object> request)
            throws ProxyAutoException {
		
    	//服务端入库
    	Map<String, Object> store=proxyCoreService.createStore(request);
    	
		//本地入库
    	Map<String,Object> model=new HashMap();
    	model.put(ClientStore.ID, UUID.randomUUID().toString());
    	model.put(ClientStore.CLIENT_MERCHANT_SN, request.get(Store.MERCHANT_ID));
    	model.put(ClientStore.CLIENT_STORE_SN, request.get(Store.CLIENT_SN));
    	model.put(ClientStore.NAME, request.get(Store.NAME));
    	storeMapDao.save(model);
    	
        return store;
    }
    @Override
    public void updateStore(Map<String, Object> request)
            throws ProxyAutoException {
    	//服务端入库
    	proxyCoreService.updateStore(request);
    	
		//本地入库
    	Map<String,Object> model=new HashMap();
    	model.put(ClientStore.ID, request.get(ClientStore.ID));
    	model.put(ClientStore.CLIENT_MERCHANT_SN, request.get(Store.MERCHANT_ID));
    	model.put(ClientStore.CLIENT_STORE_SN, request.get(Store.CLIENT_SN));
    	model.put(ClientStore.NAME, request.get(Store.NAME));
    	storeMapDao.updatePart(model);
        
    }
    @Override
    public Map<String, Object> getStore(String sn) throws ProxyAutoException {
        return proxyCoreService.getStore(sn);
    }
    @Override
    public Map<String, Object> createTerminal(Map<String, Object> request)
            throws ProxyAutoException {
    	
    	//服务端入库
    	Map<String, Object> terminal=proxyCoreService.createTerminal(request);
    	
		//本地入库
    	Map<String,Object> model=new HashMap();
    	model.put(ClientTerminal.ID, UUID.randomUUID().toString());
    	model.put(ClientTerminal.NAME, request.get(Terminal.NAME));
    	model.put(ClientTerminal.CLIENT_MERCHANT_SN, request.get(ClientTerminal.CLIENT_MERCHANT_SN));
    	model.put(ClientTerminal.CLIENT_STORE_SN, request.get(Terminal.STORE_SN));
    	model.put(ClientTerminal.CLIENT_TERMINAL_SN, request.get(Terminal.CLIENT_SN));
    	terminalMapDao.save(model);
    	
        return terminal;
        
    }
    @Override
    public void updateTerminal(Map<String, Object> request)
            throws ProxyAutoException {
        proxyCoreService.updateTerminal(request);
    }
    @Override
    public Map<String, Object> getTerminal(String sn) throws ProxyAutoException {
        return proxyCoreService.getTerminal(sn);
    }


}
