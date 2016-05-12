package com.wosai.upay.proxy.upay.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.wosai.data.bean.BeanUtil;
import com.wosai.data.dao.Dao;
import com.wosai.data.dao.DaoConstants;
import com.wosai.data.util.CollectionUtil;
import com.wosai.upay.proxy.exception.RemoteResponse400;
import com.wosai.upay.proxy.exception.RemoteResponse500;
import com.wosai.upay.proxy.upay.exception.ProxyUpayKeyStoreException;
import com.wosai.upay.proxy.upay.model.TerminalKey;

/**
 * 存储和查询终端密钥
 * @author dun
 *
 */
@Validated
public class TerminalKeyStore {
    private static final Logger logger = LoggerFactory.getLogger(TerminalKeyStore.class);

    private Dao<Map<String, Object>> terminalKeyDao;
    
    @Autowired
    private UpayApiFacade upayApi;

    @Transactional(isolation=Isolation.REPEATABLE_READ)
    public void setKey(@NotEmpty(message="终端号不能为空")String terminalSn,
                       @NotEmpty(message="密钥不能为空")String secret) {
    	Map<String, Object> existing = null;
    	try{
    		existing = terminalKeyDao.getPart(terminalSn, Collections.singleton(TerminalKey.SECRET));
    	}catch(Exception e){
    		
    	}
    	try {
        	if(existing != null) {
                existing.put(TerminalKey.SECRET, secret);
                terminalKeyDao.updatePart(existing);
            }else{
                @SuppressWarnings("unchecked")
                Map<String, Object> terminalKey = CollectionUtil.hashMap(DaoConstants.ID, terminalSn,
                                                                         TerminalKey.SECRET, secret);
    
                terminalKeyDao.save(terminalKey);
            }
    	}catch(Exception ex) {
    	    throw new ProxyUpayKeyStoreException(String.format("failed to set terminal key terminal_sn %s secret %s message %s",
    	                                                       terminalSn, secret, ex.getMessage()),
    	                                         ex);
    	}
    }

    @Transactional(isolation=Isolation.REPEATABLE_READ)
    public String getKey(@NotEmpty(message="终端号不能为空")String terminalSn) {
        Map<String, Object> existing = terminalKeyDao.get(terminalSn);
        if (existing == null) {
            throw new ProxyUpayKeyStoreException(String.format("终端%s不存在", terminalSn));
        }
        
        String currentSecret = (String)existing.get(TerminalKey.SECRET);
        if (currentSecret == null) {
            throw new ProxyUpayKeyStoreException(String.format("终端%s密钥为空", terminalSn));
        }

        // 如果有必要执行终端签到，更新终端密钥。
        long now = System.currentTimeMillis();
        long then = BeanUtil.getPropLong(existing, DaoConstants.MTIME);
        if (dayOfYear(now) != dayOfYear(then)) {
            try {
                Map<String,Object> response = upayApi.checkin(terminalSn, currentSecret, null);
                currentSecret = (String)response.get(TerminalKey.TERMINAL_KEY);
                @SuppressWarnings("unchecked")
                Map<String, Object> update = CollectionUtil.hashMap(DaoConstants.ID, terminalSn,
                                                                    TerminalKey.SECRET, currentSecret);
                try {
                    terminalKeyDao.updatePart(update);
                }catch(Exception ex) {
                    logger.warn(String.format("failed to set term %s secret to %s in local db. this is bad!", terminalSn, currentSecret), ex);
                }
            } catch (IOException ex) {
                logger.warn("network is temporarily unavailable and it's very likely your term secret has *changed*", ex);
            } catch (RemoteResponse400 ex) {
                logger.warn("upay-api CHECKIN specification might have changed.", ex);
            } catch (RemoteResponse500 ex) {
                logger.warn("upay-api is temporarily unavailable", ex);
            }

        }

        return currentSecret;
    }
    
    public void setTerminalKeyDao(Dao<Map<String, Object>> dao) {
        this.terminalKeyDao = dao;
    }
    
    private static int dayOfYear(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.DAY_OF_YEAR);
    }
}
