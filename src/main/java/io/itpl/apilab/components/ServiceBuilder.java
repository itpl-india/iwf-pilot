package io.itpl.apilab.components;

import io.itpl.apilab.accepter.PacketAccepter;
import io.itpl.apilab.broker.BrokerFactory;
import io.itpl.apilab.broker.TaskBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServiceBuilder {
    @Autowired
    BrokerFactory brokerFactory;
    private static final Logger logger = LoggerFactory.getLogger(ServiceBuilder.class);
    public PacketAccepter newPacketAcceptor(String driverId) throws IOException {
        logger.info("[{}] Creating Packet Accepter",driverId);
        PacketAccepter accepter = new DefaultPacketAccepter();
        accepter.setDriverId(driverId);
        TaskBroker taskBroker = brokerFactory.newFixedPoolSize(100,accepter.getTaskPool(),driverId);
        accepter.bind(taskBroker);
        logger.info("[{}] PacketAccepter is Ready to start!",driverId);
        return accepter;
    }
}
