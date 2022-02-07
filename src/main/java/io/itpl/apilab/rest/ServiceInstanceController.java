package io.itpl.apilab.rest;

import io.itpl.apilab.data.ApiResponse;
import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.ServiceInstance;
import io.itpl.apilab.services.ServiceInstanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ServiceInstanceController {

    @Autowired
    ServiceInstanceManager serviceInstanceManager;

    @PostMapping("/service-instance")
    public ApiResponse create(@RequestBody DeviceDriver deviceDriver){

        ApiResponse response = ApiResponse.init();
        try {
            int id = serviceInstanceManager.start(deviceDriver);
            response.submit(Integer.valueOf(id));
        }catch (IOException e){
            response.setStatus(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
    @PutMapping("/service-instance/{id}")
    public ApiResponse shutdown(@PathVariable("id") String id){
        ApiResponse response = ApiResponse.init();
        try{
            ServiceInstance payload = serviceInstanceManager.shutdown(id);
            response.setPayload(payload);
        } catch (IOException e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
    @GetMapping("/service-instance")
    public ApiResponse viewAll(){
        ApiResponse response = ApiResponse.init();
        List<ServiceInstance> payload = serviceInstanceManager.viewAll();
        response.setPayload(payload);
        return response;
    }

}
