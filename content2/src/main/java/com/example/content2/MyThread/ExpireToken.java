package com.example.content2.MyThread;

import com.example.content2.Util.TokenStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

@Component
@DependsOn("tokenMapping")
public class ExpireToken {

    private Log log = LogFactory.getLog(this.getClass());
    private ConcurrentHashMap<String, TokenStore> tokenMapping;

    @Autowired

    public ExpireToken(@Qualifier("tokenMapping") ConcurrentHashMap<String, TokenStore> tokenMapping, @Qualifier("tokenExpireTime") Long expireTime) {
        this.tokenMapping = tokenMapping;
        Thread t = new Thread(()->{
            while (true){
                int times = 0;
                int size = tokenMapping.size();
                synchronized (tokenMapping){
                    ConcurrentHashMap.KeySetView keySetView = tokenMapping.keySet();
                    Iterator<String> iterator = keySetView.iterator();
                    if (iterator.hasNext()){
                        String next = iterator.next();
                        Long createTime = tokenMapping.get(next).getCreateTime();
                        long now = System.currentTimeMillis();
                        if (now-createTime>expireTime){
                            iterator.remove();
                            times++;
                        }
                    }
                }
                log.info(Thread.currentThread().getName() + "完成一次轮询!   在{"+size+"}个token中手撕了{"+times+"}个 token");
                try {
                    Thread.sleep(1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"Token撕裂者!");
        t.setPriority(Thread.MIN_PRIORITY);
        t.setDaemon(true);
        t.start();
    }


}
