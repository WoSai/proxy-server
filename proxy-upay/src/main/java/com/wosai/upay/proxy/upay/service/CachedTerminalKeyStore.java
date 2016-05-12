package com.wosai.upay.proxy.upay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CachedTerminalKeyStore {
    @Autowired
    private TerminalKeyStore keyStore;
    
    public void setKey(String terminalSn,
                       String secret) {

        keyStore.setKey(terminalSn, secret);
    }

    @Cacheable("terminal_key")
    public String getKey(String terminalSn) {
        
        return keyStore.getKey(terminalSn);

    }
    
    @CacheEvict(value="terminal_key", allEntries=true)
    public void evictCache() {
    }

}
