package io.itpl.apilab.jmx;

import io.itpl.apilab.data.ServiceInstance;

import java.util.List;

public interface CounterViewMXBean {
    public String sayHello();
    public int process(int x,int y);
    public String getName();
    public void setName(String name);
    public int viewCounters();
    public List<ServiceInstance> viewLiveInstances();
}
