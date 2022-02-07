package io.itpl.apilab.listener;

import io.itpl.apilab.accepter.PacketAccepter;
import io.itpl.apilab.data.DeviceDriver;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

public interface DeviceListener extends Runnable {

    public void create(DeviceDriver driver,PacketAccepter handler) throws SocketException;
    public String getProtocol();
    public long receivedTotal();
    public Date startedOn();
    public long errorCount();
    public void shutdown(String id) throws IOException;
    public PacketAccepter getAcceptor();
    public String deviceDriverId();
}
