package io.itpl.apilab.services.impl;

import io.itpl.apilab.data.Packet;
import io.itpl.apilab.services.PacketCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

@Service
public class DefaultPacketCollector implements PacketCollector {
    private static final int MAX_SIMULATED_RESPONSE_TIME_MILLIS = 250;
    private static final Logger logger = LoggerFactory.getLogger(DefaultPacketCollector.class);
    private LongAdder counter = new LongAdder();
    @Override
    public void collect(Packet packet) {
        counter.increment();
        double random = Math.random();
        double calculatedResponseTime = random * MAX_SIMULATED_RESPONSE_TIME_MILLIS;
        int actualResponseTime = (int)Math.ceil(calculatedResponseTime);
        try{
            // decode();
            // filter();
            // save();
            Thread.sleep(actualResponseTime);;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            logger.info("[{}]ms, [{}] Packet Processed!!",actualResponseTime,packet.getLabel());
    }

}
