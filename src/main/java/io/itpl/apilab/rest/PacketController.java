package io.itpl.apilab.rest;

import io.itpl.apilab.data.ApiResponse;
import io.itpl.apilab.data.Packet;
import io.itpl.apilab.services.ServiceInstanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.atomic.LongAdder;

@RestController
public class PacketController {

    @Autowired
    ServiceInstanceManager serviceInstanceManager;

    private LongAdder counter = new LongAdder();
    @PostMapping("/submit")
    public ApiResponse submit(@RequestBody Packet packet){
        ApiResponse response = ApiResponse.init();
        try{
            String driverId = packet.getDriverId();
            serviceInstanceManager.submit(packet);
            counter.increment();
            response.submit(counter);
        }catch (IOException e){
            response.setStatus(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
