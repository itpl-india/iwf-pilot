package io.itpl.apilab.receiver.impl;

import io.itpl.apilab.data.Packet;
import io.itpl.apilab.receiver.PacketReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.LongAdder;

@Service
public class DefaultPacketReceiver implements PacketReceiver {
    private static final int MAX_SIMULATED_RESPONSE_TIME_MILLIS = 350;
    private static final Logger logger = LoggerFactory.getLogger(DefaultPacketReceiver.class);
    private LongAdder counter = new LongAdder();
    @Override
    public void receive(Packet packet) {
        counter.increment();
        double random = Math.random();
        double calculatedResponseTime = random * MAX_SIMULATED_RESPONSE_TIME_MILLIS;
        int actualResponseTime = (int)Math.ceil(calculatedResponseTime);
        String data = new String(packet.getChunk());
        if(data.length()>16){
            data = data.substring(0,15);
        }
        try{
            // decode();
            // filter();
            // save();

            Thread.sleep(actualResponseTime);;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.trace("[{}]ms,Packet#{} collected & saved:-[{}]",actualResponseTime,packet.getLabel(),data);
    }

}
