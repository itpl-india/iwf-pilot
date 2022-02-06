package io.itpl.apilab.components;

import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.Packet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UdpDispatcherService {
    private Map<String,UdpDispatcher> dispatchers = new HashMap<>();
    public void create(DeviceDriver deviceDriver) throws SocketException, UnknownHostException {
        String driverId = deviceDriver.getId();
        int port = deviceDriver.getPort();
        UdpDispatcher dispatcher = new UdpDispatcher();
        dispatchers.put(driverId,dispatcher);

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
}
