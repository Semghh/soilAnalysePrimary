package com.example.content2.MyThread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ClearTempExcelThread {

    private static Log log = LogFactory.getLog(ClearTempExcelThread.class);

    private final ConcurrentHashMap<File,Long> timerQueue;


    @Autowired
    public ClearTempExcelThread(ConcurrentHashMap<File, Long> timerQueue,
                                @Value("${excelTemplate.sleepTimeKeep}") String sleepTimeKeep,
                                @Value("${excelTemplate.outOfDateThreshold}")String outOfDateThreshold ) {
        this.timerQueue = timerQueue;

        ScheduledExecutorService scheduledThreadPool =
                Executors.newScheduledThreadPool(1,new MyThreadFactory());

        scheduledThreadPool.scheduleAtFixedRate(
                new scheduledRunnable(timerQueue,calculateTime(outOfDateThreshold))
                ,0,calculateTime(sleepTimeKeep), TimeUnit.MILLISECONDS);


    }

    //Task
    static class clearRunnable implements Runnable{

        private ConcurrentHashMap<File,Long> TimerQueue;
        long sleepTimeKeep;
        long outOfDateThreshold;

        public clearRunnable(ConcurrentHashMap<File, Long> TimerQueue, long sleepTimeKeep,
                long outOfDateThreshold) {
            this.TimerQueue = TimerQueue;
            this.sleepTimeKeep = sleepTimeKeep;
            this.outOfDateThreshold = outOfDateThreshold;
        }

        @Override
        public void run() {
            while (true){
                int times = 0;
                System.out.println(Thread.currentThread().getName()+"程序唤醒...");
                Set<Map.Entry<File, Long>> entries = TimerQueue.entrySet();

                Iterator<Map.Entry<File, Long>> iterator = entries.iterator();

                Queue<File> fileList = new LinkedList<>();
                while (iterator.hasNext()) {
                    Map.Entry<File, Long> next = iterator.next();
                    Long value = next.getValue();
                    if (System.currentTimeMillis()-value>outOfDateThreshold)fileList.add(next.getKey());
                }

                while (!fileList.isEmpty()){
                    File poll = fileList.poll();
                    TimerQueue.remove(poll);
                    if (poll.exists()){
                        boolean delete = poll.delete();
                        times += delete?1:0;
                    }
                }
                System.out.println(Thread.currentThread().getName()+"本次清理"+times+"个Excel...");
                try {
                    System.out.println(Thread.currentThread().getName()+"程序开始睡眠...");
                    Thread.sleep(sleepTimeKeep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class scheduledRunnable implements Runnable {

        private ConcurrentHashMap<File,Long> timerQueue;
        private long outOfDateThreshold;

        public scheduledRunnable(ConcurrentHashMap<File, Long> timerQueue,long outOfDateThreshold) {
            this.outOfDateThreshold = outOfDateThreshold;
            this.timerQueue = timerQueue;
        }

        @Override
        public void run() {

            AtomicInteger times = new AtomicInteger();

            long now = System.currentTimeMillis();

            Set<Map.Entry<File, Long>> entries = timerQueue.entrySet();
            Iterator<Map.Entry<File, Long>> iterator = entries.iterator();
            Queue<File> fileQueue = new LinkedList<>();
            while (iterator.hasNext()) {
                Map.Entry<File, Long> next = iterator.next();
                if (now-next.getValue()>outOfDateThreshold){
                    fileQueue.add(next.getKey());
                }
            }
            fileQueue.stream().forEach(x->{
                timerQueue.remove(x);
                if (x.exists() && x.delete()){
                    times.getAndIncrement();
                }
            });

            String name = Thread.currentThread().getName();
            log.info("["+name+"] : 完成一次任务,清除"+times.get()+"任务");
        }
    }

    static long calculateTime(String s){
        String[] split = s.split("\\D");
        long res = 1;
        for (String value : split) res *= Long.parseLong(value);
        return res;
    }

    static class MyThreadFactory implements ThreadFactory {

        AtomicInteger poolNumber = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r,"Excel文件撕裂者-"+poolNumber.incrementAndGet());
            t.setDaemon(false);
            return t;
        }
    }
}
