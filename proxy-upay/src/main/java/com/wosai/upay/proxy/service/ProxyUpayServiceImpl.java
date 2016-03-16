package com.wosai.upay.proxy.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wosai.upay.proxy.exception.ProxyUpayException;

/**
 * 代理终端逻辑，实现终端交易流程，以及自动签到。
 * @author dun
 *
 */
@Service
public class ProxyUpayServiceImpl implements ProxyUpayService {

    @Autowired
    private UpayApiFacade upayApi;

    @Override
    public void init(String terminalSn, String secret)
            throws ProxyUpayException {

        // 在代理服务上面保存终端密钥
        // 以后自动签到会自动更新密钥
        
    }

    @Override
    public Map<String, Object> pay(Map<String, Object> request)
            throws ProxyUpayException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> precreate(Map<String, Object> request)
            throws ProxyUpayException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> query(Map<String, Object> request)
            throws ProxyUpayException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> refund(Map<String, Object> request)
            throws ProxyUpayException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> revoke(Map<String, Object> request)
            throws ProxyUpayException {
        // TODO Auto-generated method stub
        return null;
    }

}
