package io.itpl.apilab.listener;

import io.itpl.apilab.accepter.PacketAccepter;
import io.itpl.apilab.data.DeviceDriver;

import java.net.SocketException;

public interface DeviceListener extends Runnable {

    public void create(DeviceDriver driver,PacketAccepter handler) throws SocketException;
    public void shutdown(String id);
    public PacketAccepter getAcceptor();
}