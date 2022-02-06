package io.itpl.apilab.services;

import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.Packet;

import java.io.IOException;

public interface ServiceInstanceManager {
    public int start(DeviceDriver driver) throws IOException;
    public void submit(Packet packet) throws IOException;
    public int shutdown(String driverId);
}
