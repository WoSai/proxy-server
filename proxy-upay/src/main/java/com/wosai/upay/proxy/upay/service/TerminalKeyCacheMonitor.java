package com.wosai.upay.proxy.upay.service;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 确保在日期在发生变更的时候，终端密钥缓存会清空。
 * @author dun
 *
 */
@Service
public class TerminalKeyCacheMonitor implements InitializingBean, DisposableBean{
    private static final Logger logger = LoggerFactory.getLogger(TerminalKeyCacheMonitor.class);

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    CachedTerminalKeyStore keyStore;

    @Override
    public void afterPropertiesSet() throws Exception {
        schedule();
    }

    @Override
    public void destroy() throws Exception {
        executor.shutdown();
    }

    class CacheEvictTask implements Runnable {
        public void run() {
            keyStore.evictCache();
            logger.info("evicted terminal key cache");
            schedule();
        }
    }
    
    private void schedule() {
        Calendar cal = Calendar.getInstance();
        long now = cal.getTimeInMillis();
        long nextDay = startOfNextDay(cal);
        long delay = nextDay - now;
        
        executor.schedule(new CacheEvictTask(), delay, TimeUnit.MILLISECONDS);
        
        logger.info("scheduled the next cache eviction in {} ms", new PrettyTimeFormatter(delay));
    }

    private static long startOfNextDay(Calendar cal) {
        cal.add(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    class PrettyTimeFormatter {
        private static final long MINUTE_IN_MILLIS = 60 * 1000L;
        private static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;
        private static final long DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;
        
        private long durationInMillis;
        PrettyTimeFormatter(long durationInMillis) {
            this.durationInMillis = durationInMillis;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            long remains = durationInMillis;
            long days = remains / DAY_IN_MILLIS;
            if (days > 0) {
                sb.append(days).append("d ");
                remains = remains % DAY_IN_MILLIS;
            }
            
            long hours = remains / HOUR_IN_MILLIS;
            sb.append(hours).append("h ");
            remains = remains % HOUR_IN_MILLIS;
            
            long minutes = remains / MINUTE_IN_MILLIS;
            sb.append(minutes).append("m ");
            remains = remains % MINUTE_IN_MILLIS;
            
            long seconds = remains / 1000;
            long millis = remains % 1000;
            
            sb.append(String.format("%d.%03d", seconds, millis));
            return sb.toString();
        }
    }
}
