package io.itpl.apilab.simulator;

import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class UdpDispatcherService {
    private Map<String, UdpDispatcher> dispatchers = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(UdpDispatcherService.class);
    public void create(DeviceDriver deviceDriver) throws SocketException, UnknownHostException {
        String driverId = deviceDriver.getId();
        int port = deviceDriver.getPort();
        UdpDispatcher dispatcher = new UdpDispatcher();
        dispatchers.put(driverId,dispatcher);
        logger.info("[{}] UDP dispatcher({}) started!",port,driverId);
    }
    public void send(Packet packet) throws IOException {
        String driverId = packet.getDriverId();
        if(dispatchers.containsKey(driverId)) {
            UdpDispatcher dispatcher = dispatchers.get(driverId);
            dispatcher.submit(packet);
        }else{
            throw new IOException("Udp Sender not found for driver:"+driverId);
        }
    }
    public int simulate(int qty){
        int counter = 1;
        int errors = 0;
        long start = System.currentTimeMillis();
        long accumulatedDispatchTime = 0;
        while(counter <= qty){
            Packet packet = new Packet();
            packet.setDriverId("itpl-default");
            packet.setDestinationAddress("192.168.1.90");
            packet.setSourcePort(9999);
            try {
                long time = System.currentTimeMillis();
                send(packet);
                long finish = System.currentTimeMillis();
                int sendTime = (int)(finish-time);
                accumulatedDispatchTime += sendTime;
                counter++;
            }catch (IOException e){
                errors++;
                e.printStackTrace();
                break;
            }
        }
        long finished = System.currentTimeMillis();
        int duration = (int)(finished-start);
        logger.info("[{}]ms Finished sending {} packets, avg-dispatch-time:{}ms,errors:{}",duration,counter,(accumulatedDispatchTime/counter),errors);
        return duration;
    }
    public int submitLoad(int qty, int loops){
        ExecutorService pool = Executors.newFixedThreadPool(100);
        for(int i=0;i<loops;i++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    simulate(qty);
                }
            });
        }
        return 0;
    }
}
