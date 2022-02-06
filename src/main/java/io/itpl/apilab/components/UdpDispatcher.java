package io.itpl.apilab.components;

import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UdpDispatcher {
    private DatagramSocket socket = new DatagramSocket();
    private static final Logger logger = LoggerFactory.getLogger(UdpDispatcher.class);
    public UdpDispatcher() throws SocketException {

    }


    public void submit(Packet packet) throws IOException {
        InetAddress address = InetAddress.getByName("localhost");
        byte[] buffer = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        //logger.info("Sending Message to [{}]:[{}]:[{}]",address,packet.getSourcePort(),buffer);
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length,address, packet.getSourcePort());
        try {
            this.socket.send(datagramPacket);
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }
    }
}

