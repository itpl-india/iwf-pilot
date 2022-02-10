package io.itpl.apilab.components;

import io.itpl.apilab.accepter.PacketAccepter;
import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.listener.DeviceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

public class UdpDeviceListener implements DeviceListener {
    private DeviceDriver driver;
    private Date startedOn;
    private PacketAccepter handler;
    private int port;
    private AtomicBoolean stopped = new AtomicBoolean(false);
    private DatagramSocket udpSocket;
    private static final Logger logger = LoggerFactory.getLogger(UdpDeviceListener.class);
    private LongAdder counter = new LongAdder();
    private LongAdder errors = new LongAdder();
    Thread listenerThread = null;
    @Override
    public void create(DeviceDriver driver, PacketAccepter handler) throws SocketException {
        this.driver = driver;
        this.port = driver.getPort();
        udpSocket = new DatagramSocket(this.port);
        int bufferSize = udpSocket.getSendBufferSize();
        logger.info("UDP socket created with buffer-size:{}",bufferSize);
        this.handler = handler;
    }

    @Override
    public String getProtocol() {
        return "UDP";
    }

    @Override
    public long receivedTotal() {
        return counter.longValue();
    }

    @Override
    public Date startedOn() {
        return startedOn;
    }

    @Override
    public long errorCount() {
        return errors.longValue();
    }

    @Override
    public void shutdown(String id) throws IOException {
        this.stopped.set(true);
        InetAddress address = InetAddress.getByName("localhost");
        byte[] cmd = "BYE".getBytes(StandardCharsets.UTF_8);
        DatagramPacket terminator = new DatagramPacket(cmd,cmd.length,address,port);
        udpSocket.send(terminator);
        if(listenerThread!=null){
            listenerThread.interrupt();

            if(listenerThread.isInterrupted()) {
                logger.info("[{}] Listener Thread({}) shutdown initiated.", this.port,listenerThread.getName());
            }
        }
    }

    @Override
    public PacketAccepter getAcceptor() {
        return this.handler;
    }

    @Override
    public String deviceDriverId() {
        return this.driver.getId();
    }

    @Override
    public void run() {
        this.startedOn = new Date();
        logger.info("[{}] UDP Listener Started!",this.port);
        listenerThread = Thread.currentThread();
        while (!stopped.get()){

            byte[] buffer = new byte[65535];

            DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
            try {

                udpSocket.receive(packet);
                long start = System.currentTimeMillis();
                if(isShutdownPacket(buffer)){
                    if(Thread.currentThread().isInterrupted()){
                        logger.info("[{}] Shutting down UDP listener thread.",port);
                        break;
                    }
                }
                handler.accept(buffer, InetAddress.getLocalHost().getHostAddress(), this.port);
                counter.increment();
                logger.info("[{}]ms Packet#{} Received on [Port:{}]",System.currentTimeMillis()-start,counter.longValue(),this.port);
            } catch (IOException e) {
                logger.error("[{}] error while receiving UDP Packet: {}",this.port,e.getMessage());
                errors.increment();
            }
        }
        udpSocket.close();
        logger.info("[{}] UDP Listener Stopped!",this.port);

    }
    private boolean isShutdownPacket(byte[] chunk){
        if(chunk==null) return false;
        if(chunk.length<3) return false;
        String cmd = new String(chunk,0,1);
        return cmd.equals("BYE");
    }
}
