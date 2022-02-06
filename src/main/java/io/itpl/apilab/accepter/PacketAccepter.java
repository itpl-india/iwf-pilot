package io.itpl.apilab.accepter;

import io.itpl.apilab.data.Packet;

import java.io.IOException;
import java.util.Queue;

public interface PacketAccepter {

    public void setDriverId(String id);
    public void accept(byte[] packet,String source,int port);
    public void bind(Runnable broker) throws IOException;
    public void start();
    public void stop();
    public Queue<Packet> getTaskPool();
}
