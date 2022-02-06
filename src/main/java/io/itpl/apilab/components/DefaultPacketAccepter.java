package io.itpl.apilab.components;

import io.itpl.apilab.data.Packet;
import io.itpl.apilab.accepter.PacketAccepter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.LongAdder;


public class DefaultPacketAccepter implements PacketAccepter {
    private Queue<Packet> taskPool = new ArrayDeque<>();
    private String driverId;
    private Thread brokerThread;
    private boolean online;
    private static final Logger logger = LoggerFactory.getLogger(DefaultPacketAccepter.class);
    private LongAdder counter = new LongAdder();
    @Override
    public void setDriverId(String id) {
        this.driverId = id;
    }

    @Override
    public void accept(byte[] packet,String source,int port) {
        if(!online){
            logger.error("Task Broker is not online");
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
        brokerThread.start();
        this.online = true;
    }

    @Override
    public void stop() {
        brokerThread.interrupt();
    }

    @Override
    public Queue<Packet> getTaskPool() {
        return taskPool;
    }
}
