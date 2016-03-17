package com.wosai.upay.proxy.auto.service;

import java.util.Map;

import com.wosai.data.dao.Dao;

public class ProxyObjectMap {

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
        
    }
    
    /**
     * 从映射表中查询terminal_sn（系统分配的终端序列号，调用proxy-core创建终端的时候返回）
     * @param clientMerchantSn
     * @param clientTerminalSn
     * @return
     */
    public String getTerminalSn(String clientMerchantSn, String clientTerminalSn) {
        return null;
    }

    public void setStoreMapDao(Dao<Map<String, Object>> dao) {
        this.storeMapDao = dao;
    }
    public void setTerminalMapDao(Dao<Map<String, Object>> dao) {
        this.terminalMapDao = dao;
    }

    public static enum Advice {
        NOOP,                           // 门店和终端在映射表中存在，并且归属关系没有变化
        CREATE_STORE_AND_MOVE_TERMINAL, // 门店不存在但是终端存在。创建门店改变终端归属
        CREATE_STORE_AND_TERMINAL,      // 门店和终端都不存在。创建门店和终端
        CREATE_TERMINAL,                // 门店存在但是终端不存在。创建终端。
        MOVE_TERMINAL,                  // 门店和终端都存在。需要改变终端归属
    }
}
