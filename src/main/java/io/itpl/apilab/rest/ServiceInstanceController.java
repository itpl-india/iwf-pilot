package io.itpl.apilab.rest;

import io.itpl.apilab.data.ApiResponse;
import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.services.ServiceInstanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

}
