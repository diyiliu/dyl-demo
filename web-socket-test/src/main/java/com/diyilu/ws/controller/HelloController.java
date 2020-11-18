package com.diyilu.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: HelloController
 * Author: diyiliu
 * Update: 2020-11-17 17:19
 */

@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String index()  {

        return "index";
    }

    @GetMapping("/ws")
    public String socket()  {

        return "socket";
    }
}
