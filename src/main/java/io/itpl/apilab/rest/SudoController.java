package io.itpl.apilab.rest;

import io.itpl.apilab.data.ApiResponse;
import io.itpl.apilab.accepter.PacketAccepter;
import io.itpl.apilab.services.SudoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SudoController {



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
