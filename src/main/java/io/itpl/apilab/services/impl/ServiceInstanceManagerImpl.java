package io.itpl.apilab.services.impl;

import io.itpl.apilab.accepter.PacketAccepter;
import io.itpl.apilab.poller.PollerFactory;
import io.itpl.apilab.components.UdpDeviceListener;
import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.Packet;
import io.itpl.apilab.data.ServiceInstance;
import io.itpl.apilab.listener.DeviceListener;
import io.itpl.apilab.services.ServiceInstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ServiceInstanceManagerImpl implements ServiceInstanceManager {

    @Autowired
    PollerFactory brokerFactory;

    private Map<String, DeviceListener> serviceInstances = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceManagerImpl.class);
    @Override
    public int start(DeviceDriver driver) throws IOException {
        String driverId = driver.getId();
        logger.info("[{}] Creating new Service Instance.",driverId);
        PacketAccepter accepter = brokerFactory.newPacketAcceptor(driver);
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
    public ServiceInstance shutdown(String driverId) throws IOException {
        if(serviceInstances.containsKey(driverId)){
            DeviceListener deviceListener = serviceInstances.get(driverId);
            PacketAccepter accepter = deviceListener.getAcceptor();
            deviceListener.shutdown(driverId);
            PacketAccepter serviceInstance = deviceListener.getAcceptor();
            serviceInstance.stop();
            serviceInstances.remove(driverId);
            ServiceInstance item = new ServiceInstance();
            item.setProtocol(deviceListener.getProtocol());
            item.setReceived(accepter.received());
            item.setError(accepter.errors());
            item.setStartedOn(accepter.startedSince());
            item.setDeviceDriver(deviceListener.deviceDriverId());
            item.setInQueue(accepter.currentQueue());
            return item;
        }
        throw new IOException("Requested service instance [driver-id:"+driverId+"] not found.");
    }

    @Override
    public List<ServiceInstance> viewAll() {
        List<ServiceInstance> response = new ArrayList<>();
        if(serviceInstances.isEmpty()){
            return response;
        }
        serviceInstances.keySet().forEach(key->{
            DeviceListener instance = serviceInstances.get(key);
            PacketAccepter accepter = instance.getAcceptor();
            ServiceInstance item = new ServiceInstance();
            item.setProtocol(instance.getProtocol());
            item.setReceived(accepter.received());
            item.setError(accepter.errors());
            item.setStartedOn(accepter.startedSince());
            item.setDeviceDriver(instance.deviceDriverId());
            item.setInQueue(accepter.currentQueue());
            response.add(item);
        });
        return response;
    }
}
