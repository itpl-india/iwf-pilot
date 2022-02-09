package io.itpl.apilab.simulator;

import io.itpl.apilab.data.ApiResponse;
import io.itpl.apilab.data.DeviceDriver;
import io.itpl.apilab.data.Packet;
import io.itpl.apilab.services.SudoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.LongAdder;

public class UdpDispatcher {
    private DatagramSocket socket = new DatagramSocket();
    private static final Logger logger = LoggerFactory.getLogger(UdpDispatcher.class);
    private LongAdder counter = new LongAdder();
    public UdpDispatcher() throws SocketException {

    }


    public void submit(Packet packet) throws IOException {
        String ipAddress = packet.getDestinationAddress();
        InetAddress address = InetAddress.getByName(ipAddress);
        byte[] buffer = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        //logger.info("Sending Message to [{}]:[{}]:[{}]",address,packet.getSourcePort(),buffer);
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length,address, packet.getSourcePort());
        try {
            this.socket.send(datagramPacket);
            counter.increment();
            logger.info("[{}] Packets dispatched [{}:{}]",counter.longValue(),ipAddress,packet.getSourcePort());
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }
    }

    @RestController
    public static class UdpController {
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

    @RestController
    public static class SudoController {



        @Autowired
        SudoService service;
        private static final Logger logger = LoggerFactory.getLogger(SudoController.class);
        @GetMapping("/example")
        public ApiResponse execute(){
            ApiResponse response = ApiResponse.init();
            Integer payload = Integer.valueOf(service.execute());
            response.submit(payload);
            logger.info("[{}]ms Api Executed!",response.getResponseTime());
            return response;
        }



    }
}

