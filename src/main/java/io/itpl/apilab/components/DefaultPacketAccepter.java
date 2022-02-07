package io.itpl.apilab.components;

import io.itpl.apilab.data.Packet;
import io.itpl.apilab.accepter.PacketAccepter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.LongAdder;


public class DefaultPacketAccepter implements PacketAccepter {
    private Queue<Packet> taskPool = new ConcurrentLinkedDeque<>();
    private String driverId;
    private Thread brokerThread;
    private boolean online;
    private static final Logger logger = LoggerFactory.getLogger(DefaultPacketAccepter.class);
    private LongAdder counter = new LongAdder();
    private Date startedOn;
    private LongAdder errors = new LongAdder();

    @Override
    public void setDriverId(String id) {
        this.driverId = id;
    }

    @Override
    public void accept(byte[] packet,String source,int port) {
        if(!online){
            logger.error("Task Broker is not online");
            errors.increment();
            return;
        }
        if(!brokerThread.isAlive()){
            logger.error("Task Broker is offline");
            return;
        }
        Packet task = new Packet();
        task.setChunk(packet);
        task.setDriverId(driverId);
        task.setSourceIpAddress(source);
        task.setSourcePort(port);
        task.setLabel(counter.toString());
        taskPool.add(task);
        counter.increment();
        logger.info("[{}]New Packet added to queue [{}]",counter.longValue(),taskPool.size());
    }

    @Override
    public void bind(Runnable broker) throws IOException {
        if(this.brokerThread == null){
            brokerThread = new Thread(broker);
            brokerThread.setName(driverId+"-acct");
        }else{
            throw new IOException("broker-thread is not ready!");
        }
    }

    @Override
    public void start() {
        brokerThread.setDaemon(true);
        brokerThread.start();

        startedOn = new Date();
        this.online = true;
    }

    @Override
    public void stop() {
        brokerThread.interrupt();
        logger.info("[{}] Packet Accepter Shutdown Requested",driverId);
    }

    @Override
    public Queue<Packet> getTaskPool() {
        return taskPool;
    }

    @Override
    public Date startedSince() {
        return startedOn;
    }

    @Override
    public long received() {
        return counter.longValue();
    }

    @Override
    public long errors() {
        return errors.longValue();
    }

    @Override
    public long scheduled() {
        return counter.longValue();
    }

    @Override
    public long currentQueue() {
        return taskPool.size();
    }
}
