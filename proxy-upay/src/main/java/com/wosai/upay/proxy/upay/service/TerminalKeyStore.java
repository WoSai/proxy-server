package com.wosai.upay.proxy.upay.service;

import java.util.Collections;
import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.wosai.data.dao.Dao;
import com.wosai.data.dao.DaoConstants;
import com.wosai.data.util.CollectionUtil;
import com.wosai.upay.proxy.upay.model.TerminalKey;

/**
 * 存储和查询终端密钥
 * @author dun
 *
 */
@Validated
public class TerminalKeyStore {
    private Dao<Map<String, Object>> terminalKeyDao;

    @Transactional
    public void setKey(@NotEmpty(message="终端号不能为空")String terminalSn,
                       @NotEmpty(message="密钥不能为空")String secret) {

        Map<String, Object> existing = terminalKeyDao.getPart(terminalSn, Collections.singleton(TerminalKey.SECRET));
        if(existing != null) {
            existing.put(TerminalKey.SECRET, secret);
            terminalKeyDao.updatePart(existing);
        }else{
            @SuppressWarnings("unchecked")
            Map<String, Object> terminalKey = CollectionUtil.hashMap(DaoConstants.ID, terminalSn,
                                                                     TerminalKey.SECRET, secret);

            terminalKeyDao.save(terminalKey);
        }
    }

    @Cacheable("terminal_key")
    public String getKey(@NotEmpty(message="终端号不能为空")String terminalSn) {
        Map<String, Object> existing = terminalKeyDao.getPart(terminalSn, Collections.singleton(TerminalKey.SECRET));
        if (existing == null) {
            return null;
        }
        return (String)existing.get(TerminalKey.SECRET);
    }

    public void setTerminalKeyDao(Dao<Map<String, Object>> dao) {
        this.terminalKeyDao = dao;
    }
}
