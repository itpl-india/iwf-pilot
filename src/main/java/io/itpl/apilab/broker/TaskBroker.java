package io.itpl.apilab.broker;

import io.itpl.apilab.data.Packet;
import io.itpl.apilab.services.PacketCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;


public class TaskBroker implements Runnable{
    private String driverId;
    private Queue<Packet> taskPool;
    private int workerThreadPoolSize;
    private ExecutorService workerThreadPool;
    private PacketCollector collector;
    private boolean ready;
    private boolean stopped;
    private static final Logger logger = LoggerFactory.getLogger(TaskBroker.class);
    private LongAdder counter = new LongAdder();
    private TaskBroker(){}
    public static TaskBroker getInstance(int poolSize,Queue<Packet> taskPool,String driverId){
        logger.info("[{}] Creating Task Broker",driverId);
        TaskBroker broker = new TaskBroker();
        broker.taskPool = taskPool;
        broker.workerThreadPoolSize = poolSize;
        broker.driverId = driverId;
        return broker;
    }
    public void build(PacketCollector collector){
        workerThreadPool = Executors.newFixedThreadPool(workerThreadPoolSize);
        this.collector = collector;
        this.ready = true;
    }
    @Override
    public void run() {
        logger.info("[{}] Task Broker Started",this.driverId);
        if(!ready){
            logger.error("[{}] Task Broker is Not Ready!",this.driverId);
            return;
        }
        while (!stopped){
            if(!taskPool.isEmpty()){
                counter.increment();
                logger.trace("[{}] [{}] Scheduling new Task",driverId,counter.longValue());
                PacketCollectionTask task = new PacketCollectionTask(taskPool.poll(),collector);
                workerThreadPool.submit(task);
            }
        }
    }
    public void stop(){
        this.stopped = true;
    }

    public String getDriverId() {
        return driverId;
    }
}
