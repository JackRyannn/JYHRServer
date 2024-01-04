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
    public void uploadHzjbxx() {
        System.out.println("uploadHzjbxx");
        jyhrService.hzjbxx();
    }

    @RequestMapping(value = "/uploadJybg", method = RequestMethod.GET)
    public void uploadJybg() {
        System.out.println("uploadJybg");
        jyhrService.jybg();
    }

    @RequestMapping(value = "/uploadJybgxq", method = RequestMethod.GET)
    public void uploadJybgxq() {
        System.out.println("uploadJybgxq");
        jyhrService.jybgxq();
    }
}
