package com.yhdyy.jyhrserver.controllers;

import com.yhdyy.jyhrserver.services.JyhrService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class JyhrController {
    @Resource
    private JyhrService jyhrService;

    @RequestMapping(value = "/uploadHzjbxx", method = RequestMethod.GET)
    public void uploadHzjbxx(String startTime) {
        System.out.println("uploadHzjbxx");
        jyhrService.hzjbxx(startTime);
    }

    @RequestMapping(value = "/uploadJybg", method = RequestMethod.GET)
    public void uploadJybg(String startTime) {
        System.out.println("uploadJybg");
        jyhrService.jybg(startTime);
    }

    @RequestMapping(value = "/uploadJybgxq", method = RequestMethod.GET)
    public void uploadJybgxq(String startTime) {
        System.out.println("uploadJybgxq");
        jyhrService.jybgxq(startTime);
    }
}
