package io.itpl.apilab.rest;

import io.itpl.apilab.components.UdpDispatcherService;
import io.itpl.apilab.data.ApiResponse;
import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

@RestController
public class UdpController {
    @Autowired
    UdpDispatcherService udpDispatcherService;

    @PostMapping("/udp")
    public ApiResponse create(@RequestBody DeviceDriver deviceDriver){
        ApiResponse response = ApiResponse.init();
        try{
            udpDispatcherService.create(deviceDriver);
            response.submit(deviceDriver);

        } catch (SocketException e) {
            response.setStatus(401);
            response.setMessage(e.getMessage());
        } catch (UnknownHostException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        }
        return response;
    }
    @PostMapping("/udp/submit")
    public ApiResponse submit(@RequestBody Packet packet){
        ApiResponse response = ApiResponse.init();
        try {
            udpDispatcherService.send(packet);
            response.submit(packet);
        } catch (IOException e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
