package com.wosai.upay.proxy.upay.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wosai.upay.proxy.upay.exception.ProxyUpayException;
import com.wosai.upay.proxy.upay.model.Order;

/**
 * 代理终端逻辑
 * <ul>
 * <li>实现终端交易流程（轮询、自动撤单）</li>
 * <li>初始化终端密钥</li>
 * <li>交易触发自动签到</li>
 * </ul>
 * @author dun
 *
 */
@Service
public class ProxyUpayServiceImpl implements ProxyUpayService {

    @Autowired
    private UpayApiFacade upayApi;
    
    @Autowired
    private TerminalKeyStore keyStore;

    @Override
    public void init(String terminalSn, String secret)
            throws ProxyUpayException {

        // 初始化终端密钥
        // 以后自动签到会更新密钥
        keyStore.setKey(terminalSn, secret);
        
    }

    @Override
    public Map<String, Object> pay(Map<String, Object> request)
            throws ProxyUpayException {

        // B扫C终端流程
        String terminalSn = (String)request.get(Order.TERMINAL_SN);
        String terminalKey = keyStore.getKey(terminalSn);
        Map<String, Object> result = upayApi.pay(terminalSn,
                                                 terminalKey,
                                                 request);

        return null;
    }

    @Override
    public Map<String, Object> precreate(Map<String, Object> request)
            throws ProxyUpayException {

        // C扫B入口
        return null;
    }

    @Override
    public Map<String, Object> query(Map<String, Object> request)
            throws ProxyUpayException {

        // 查询
        return null;
    }

    @Override
    public Map<String, Object> refund(Map<String, Object> request)
            throws ProxyUpayException {

        // 退款
        return null;
    }

    @Override
    public Map<String, Object> revoke(Map<String, Object> request)
            throws ProxyUpayException {

        // 手动撤单
        return null;
    }

}
