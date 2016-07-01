package com.wosai.upay.proxy.single.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CachedTerminalKeyStore {
    
    @CacheEvict(value="terminal_key", allEntries=true)
    public void evictCache() {
    }

}
