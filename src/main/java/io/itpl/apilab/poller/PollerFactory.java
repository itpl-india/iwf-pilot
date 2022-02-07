package io.itpl.apilab.poller;

import io.itpl.apilab.accepter.PacketAccepter;
import io.itpl.apilab.components.DefaultPacketAccepter;
import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.Packet;
import io.itpl.apilab.receiver.PacketReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Queue;

@Component
public class PollerFactory {
    public static final int DEFAULT_CONCURRENCY = 100;
    @Autowired
    PacketReceiver packetCollector;
    private static final Logger logger = LoggerFactory.getLogger(PollerFactory.class);
    public TaskPoller newTaskBroker(int size, Queue<Packet> source, String driverId){
        TaskPoller broker = TaskPoller.getInstance(size,source,driverId);
        logger.info("[{}] Initializing Broker Factory",driverId);
        broker.build(packetCollector);
        logger.info("[{}] Broker Factory is Ready!",driverId);
        return broker;
    }
    public PacketAccepter newPacketAcceptor(DeviceDriver driver) throws IOException {
        String driverId = driver.getId();
        int concurrentCapacity = driver.getConcurrency();
        if(concurrentCapacity <=0){
            concurrentCapacity = DEFAULT_CONCURRENCY;
        }
        logger.info("[{}] Creating Packet Accepter",driverId);
        PacketAccepter accepter = new DefaultPacketAccepter();
        accepter.setDriverId(driverId);
        TaskPoller taskBroker = newTaskBroker(concurrentCapacity,accepter.getTaskPool(),driverId);
        accepter.bind(taskBroker);
        logger.info("[{}] PacketAccepter is Ready to start!",driverId);
        return accepter;
    }
}
