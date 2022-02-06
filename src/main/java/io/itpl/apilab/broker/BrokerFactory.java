package io.itpl.apilab.broker;

import io.itpl.apilab.data.Packet;
import io.itpl.apilab.services.PacketCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;

@Component
public class BrokerFactory {

    @Autowired
    PacketCollector packetCollector;
    private static final Logger logger = LoggerFactory.getLogger(BrokerFactory.class);
    public TaskBroker newFixedPoolSize(int size, Queue<Packet> source,String driverId){
        TaskBroker broker = TaskBroker.getInstance(size,source,driverId);
        logger.info("[{}] Initializing Broker Factory",driverId);
        broker.build(packetCollector);
        logger.info("[{}] Broker Factory is Ready!",driverId);
        return broker;
    }
}
