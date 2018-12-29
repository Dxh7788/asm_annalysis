package com.asm.thread.multi;

import com.asm.util.HttpClientUtil;

import java.text.NumberFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用线程池模拟并发
 * @author dongxiaohong
 * @date 2018/12/29 10:00
 */
public class MultiThreadSimulation {

    static int count = 0;
    // 总访问量是clientNum，并发量是threadNum
    int threadNum = 4;
    int clientNum = 20000;

    float avgExecTime = 0;
    float sumexecTime = 0;
    long firstExecTime = Long.MAX_VALUE;
    long lastDoneTime = Long.MIN_VALUE;
    float totalExecTime = 0;

    public static void main(String[] args) {
        new MultiThreadSimulation().run();
    }


    public void run(){
        final ConcurrentHashMap<Integer, ThreadRecord> records = new ConcurrentHashMap<Integer, ThreadRecord>();

        // 建立ExecutorService线程池，threadNum个线程可以同时访问
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 模拟clientNum个客户端访问
        final CountDownLatch doneSignal = new CountDownLatch(clientNum);

        for (int i = 0; i < clientNum; i++) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    int index = getIndex();
                    long systemCurrentTimeMillis = System.currentTimeMillis();
                    try {
                        HttpClientUtil.sendGet("http://localhost:8080/api/3.0/m/concern", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    records.put(index, new ThreadRecord(systemCurrentTimeMillis, System.currentTimeMillis()));
                    doneSignal.countDown();// 每调用一次countDown()方法，计数器减1
                }
            };
            exec.execute(run);
        }

        try {
            // 计数器大于0 时，await()方法会阻塞程序继续执行
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 获取每个线程的开始时间和结束时间
         */
        for (int i : records.keySet()) {
            ThreadRecord r = records.get(i);
            sumexecTime += ((double) (r.endTime - r.startTime)) / 1000;

            if (r.startTime < firstExecTime) {
                firstExecTime = r.startTime;
            }
            if (r.endTime > lastDoneTime) {
                this.lastDoneTime = r.endTime;
            }
        }

        this.avgExecTime = this.sumexecTime / records.size();
        this.totalExecTime = ((float) (this.lastDoneTime - this.firstExecTime)) / 1000;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(4);

        System.out.println("======================================================");
        System.out.println("线程数量:\t\t" + threadNum);
        System.out.println("客户端数量:\t" + clientNum);
        System.out.println("平均执行时间:\t" + nf.format(this.avgExecTime) + "秒");
        System.out.println("总执行时间:\t" + nf.format(this.totalExecTime) + "秒");
        System.out.println("吞吐量:\t\t" + nf.format(this.clientNum / this.totalExecTime) + "次每秒");
    }
    public static int getIndex() {
        return ++count;
    }
}
class ThreadRecord {
    long startTime;
    long endTime;

    public ThreadRecord(long st, long et) {
        this.startTime = st;
        this.endTime = et;
    }

}
