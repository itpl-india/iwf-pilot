package io.itpl.apilab.data;

import java.util.Date;

public class ServiceInstance {
    private Date startedOn;
    private String protocol;
    private long received;
    private long scheduled;
    private long error;
    private long inQueue;
    private String deviceDriver;

    public long getInQueue() {
        return inQueue;
    }

    public void setInQueue(long inQueue) {
        this.inQueue = inQueue;
    }

    public String getDeviceDriver() {
        return deviceDriver;
    }

    public void setDeviceDriver(String deviceDriver) {
        this.deviceDriver = deviceDriver;
    }

    public Date getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(Date startedOn) {
        this.startedOn = startedOn;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public long getReceived() {
        return received;
    }

    public void setReceived(long received) {
        this.received = received;
    }

    public long getScheduled() {
        return scheduled;
    }

    public void setScheduled(long scheduled) {
        this.scheduled = scheduled;
    }

    public long getError() {
        return error;
    }

    public void setError(long error) {
        this.error = error;
    }
}
