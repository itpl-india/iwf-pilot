package io.itpl.apilab.components;

import io.itpl.apilab.accepter.PacketAccepter;
import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.listener.DeviceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpDeviceListener implements DeviceListener {
    private DeviceDriver driver;
    private PacketAccepter handler;
    private int port;
    private boolean stopped;
    private DatagramSocket udpSocket;
    private static final Logger logger = LoggerFactory.getLogger(UdpDeviceListener.class);
    @Override
    public void create(DeviceDriver driver, PacketAccepter handler) throws SocketException {
        this.port = driver.getPort();
        udpSocket = new DatagramSocket(this.port);
        this.handler = handler;
    }

    @Override
    public void shutdown(String id) {
        this.stopped = true;

    }

    @Override
    public PacketAccepter getAcceptor() {
        return this.handler;
    }

    @Override
    public void run() {
        logger.info("[{}] UDP Listener Started!",this.port);
        while (!stopped){
            byte[] buffer = new byte[65535];

            DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
            try {
                udpSocket.receive(packet);
                handler.accept(buffer, InetAddress.getLocalHost().getHostAddress(), this.port);
            } catch (IOException e) {
                logger.error("[{}] error while receiving UDP Packet: {}",this.port,e.getMessage());
            }
        }
        udpSocket.close();
        logger.info("[{}] UDP Listener Stopped!",this.port);

    }
}
