package io.itpl.apilab.jmx;

import io.itpl.apilab.ApiLabApplication;
import io.itpl.apilab.data.ServiceInstance;
import io.itpl.apilab.services.ServiceInstanceManager;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class CounterView implements CounterViewMBean {

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
