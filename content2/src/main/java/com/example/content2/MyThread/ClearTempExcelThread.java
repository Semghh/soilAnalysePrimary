package com.example.content2.MyThread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClearTempExcelThread {


    private ConcurrentHashMap<File,Long> timerQueue;


    @Autowired
    public ClearTempExcelThread(ConcurrentHashMap<File, Long> timerQueue,
                                @Value("${excelTemplate.sleepTimeKeep}") String sleepTimeKeep,
                                @Value("${excelTemplate.outOfDateThreshold}")String outOfDateThreshold ) {
        this.timerQueue = timerQueue;

        new Thread(new clearRunnable(this.timerQueue,
                calculateTime(sleepTimeKeep),
                calculateTime(outOfDateThreshold)),
                "[tempExcel撕裂者]").start();
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
//                System.out.println(Thread.currentThread().getName()+"程序唤醒...");
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
//                System.out.println(Thread.currentThread().getName()+"本次清理"+times+"个Excel...");
                try {
//                    System.out.println(Thread.currentThread().getName()+"程序开始睡眠...");
                    Thread.sleep(sleepTimeKeep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static long calculateTime(String s){
        String[] split = s.split("\\D");
        long res = 1;
        for (int i = 0; i < split.length; i++)
            res *=Long.parseLong(split[i]);
        return res;
    }
}
