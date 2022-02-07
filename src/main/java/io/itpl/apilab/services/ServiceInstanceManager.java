package io.itpl.apilab.services;

import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.Packet;
import io.itpl.apilab.data.ServiceInstance;

import java.io.IOException;
import java.util.List;

public interface ServiceInstanceManager {
    public int start(DeviceDriver driver) throws IOException;
    public void submit(Packet packet) throws IOException;
    public ServiceInstance shutdown(String driverId) throws IOException;
    public List<ServiceInstance> viewAll();
}
