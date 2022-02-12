package io.itpl.apilab.jmx;

import io.itpl.apilab.ApiLabApplication;
import io.itpl.apilab.data.ServiceInstance;
import io.itpl.apilab.services.ServiceInstanceManager;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class CounterView implements CounterViewMXBean {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int viewCounters() {

        try{
            ApplicationContext context = ApiLabApplication.getContext();
            ServiceInstanceManager manager = context.getBean(ServiceInstanceManager.class);
            List<ServiceInstance> list = manager.viewAll();
            if(list == null || list.isEmpty()){
                return -1;
            }
            ServiceInstance first = list.get(0);

            return (int) first.getReceived();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    @Override
    public List<ServiceInstance> viewLiveInstances() {
        try{
            ApplicationContext context = ApiLabApplication.getContext();
            ServiceInstanceManager manager = context.getBean(ServiceInstanceManager.class);
            List<ServiceInstance> list = manager.viewAll();
           return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String name;

    @Override
    public String sayHello() {
        return "Hello World!";
    }

    @Override
    public int process(int x, int y) {
        return x+y;
    }
}
