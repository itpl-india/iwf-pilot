package io.itpl.apilab.services.impl;

import io.itpl.apilab.services.SudoService;
import org.springframework.stereotype.Service;

@Service
public class SudoServiceImpl implements SudoService {
    @Override
    public int execute() {
        double multiplier = Math.random() * 1000;
        int responseTime = (int)Math.ceil(multiplier);
        try{
            Thread.sleep(100);
        }catch (InterruptedException e){}
        return responseTime;
    }
}
