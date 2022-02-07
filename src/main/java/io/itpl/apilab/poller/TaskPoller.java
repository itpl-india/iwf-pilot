package io.itpl.apilab.poller;

import io.itpl.apilab.data.Packet;
import io.itpl.apilab.receiver.PacketReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;


public class TaskPoller implements Runnable{
    private String driverId;
    private Queue<Packet> taskPool;
    private int workerThreadPoolSize;
    private ExecutorService workerThreadPool;
    private PacketReceiver collector;
    private boolean ready;
    private boolean stopped;
    private static final Logger logger = LoggerFactory.getLogger(TaskPoller.class);
    private LongAdder counter = new LongAdder();
    private TaskPoller(){}
    public static TaskPoller getInstance(int poolSize, Queue<Packet> taskPool, String driverId){
        logger.info("[{}] Creating Task Broker",driverId);
        TaskPoller broker = new TaskPoller();
        broker.taskPool = taskPool;
        broker.workerThreadPoolSize = poolSize;
        broker.driverId = driverId;
        return broker;
    }
    public void build(PacketReceiver collector){
        workerThreadPool = Executors.newFixedThreadPool(workerThreadPoolSize);
        this.collector = collector;
        this.ready = true;
    }
    @Override
    public void run() {
        logger.info("[{}] Task Broker Started with {} Concurrency Level",this.driverId,workerThreadPoolSize);
        if(!ready){
            logger.error("[{}] Task Broker is Not Ready!",this.driverId);
            return;
        }
        while (!stopped){
            if(!Thread.currentThread().isInterrupted()) {
                if (taskPool.isEmpty()) {
                    continue;
                }
                Packet next = taskPool.poll();
                counter.increment();
                logger.trace("[{}] Polling success, Scheduling Task#{}", driverId, counter.longValue());
                PacketReceiverTask task = new PacketReceiverTask(next, collector);
                workerThreadPool.submit(task);
            }else{
                break;
            }

        }
        logger.info("Packet Poller shutdown finished!");
    }
    public void stop(){
        this.stopped = true;
        logger.info("[{}] Packet Poller stopped.",driverId);
    }

    public String getDriverId() {
        return driverId;
    }
}
