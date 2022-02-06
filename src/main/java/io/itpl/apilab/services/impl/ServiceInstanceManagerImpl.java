package io.itpl.apilab.services.impl;

import io.itpl.apilab.accepter.PacketAccepter;
import io.itpl.apilab.components.ServiceBuilder;
import io.itpl.apilab.components.UdpDeviceListener;
import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.Packet;
import io.itpl.apilab.listener.DeviceListener;
import io.itpl.apilab.services.ServiceInstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ServiceInstanceManagerImpl implements ServiceInstanceManager {

    @Autowired
    ServiceBuilder serviceBuilder;

    private Map<String, DeviceListener> serviceInstances = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceManagerImpl.class);
    @Override
    public int start(DeviceDriver driver) throws IOException {
        String driverId = driver.getId();
        logger.info("[{}] Creating new Service Instance.",driverId);
        PacketAccepter accepter = serviceBuilder.newPacketAcceptor(driverId);
        DeviceListener listener = new UdpDeviceListener();
        listener.create(driver,accepter);
        Thread listenerThread = new Thread(listener);
        listenerThread.setName(driverId+"-"+driver.getPort());
        listenerThread.start();
        accepter.start();
        serviceInstances.put(driverId,listener);
        return serviceInstances.size();
    }

    @Override
    public void submit(Packet packet) throws IOException {
        String driverId = packet.getDriverId();
        if(serviceInstances.containsKey(driverId)){
            byte[] randomChunk = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
            packet.setChunk(randomChunk);
            DeviceListener listener = serviceInstances.get(driverId);
            PacketAccepter accepter = listener.getAcceptor();
            accepter.accept(randomChunk,"10.115.2.36",8080);
        }else{
            throw new IOException("Acceptor not available for requested driver-"+driverId);
        }
    }

    @Override
    public int shutdown(String driverId) {
        if(serviceInstances.containsKey(driverId)){
            DeviceListener deviceListener = serviceInstances.get(driverId);
            deviceListener.shutdown(driverId);
            PacketAccepter serviceInstance = deviceListener.getAcceptor();
            serviceInstance.stop();
            serviceInstances.remove(driverId);
        }
        return serviceInstances.size();
    }
}
